# Step 1: Protocol and Network — What We Built and Why

This document explains the first four pieces we implemented in plain terms and how they fit into PeerDrop.

---

## 1. `network/SocketConnection.java` — Safe Sockets

**What it does:**  
Wraps a TCP `Socket` and gives you buffered input/output streams. When you're done, you call `close()` and the socket is shut down.

**The code in simple terms:**
- **Constructor:** Stores the socket we're wrapping.
- **getInputStream() / getOutputStream():** Return streams that read/write through the socket using a fixed buffer size (8 KB from `ProtocolConstants`). Buffering means we don't do one tiny read/write per byte — we do bigger chunks, which is faster on the LAN.
- **close():** Calls `socket.close()`. We catch `IOException` so that closing never throws; that way you can safely use it in a `try`-with-resources or `finally` block without leaking connections.

**Why it matters for the project:**  
Every file transfer and every peer connection will use a socket. This class is the single place where we create buffered streams and close the socket. Later, `PeerClient`, `ConnectionHandler`, `OutgoingTransfer`, and `IncomingTransfer` will all use `SocketConnection` so we don't leave sockets open or use unbuffered I/O.

---

## 2. `protocol/ChunkRequest.java` — “Send Me This Chunk”

**What it does:**  
A small object that means: “I want chunk number X of file Y.”

**The code in simple terms:**
- **fileId:** A string that identifies the file (e.g. relative path or an id from a file list). The sender uses this to find the file on disk.
- **chunkIndex:** Which chunk we want (0 = first, 1 = second, …).
- **Constructor and getters:** So we can create a request and read its fields when we serialize it over the wire.

**Why it matters for the project:**  
We don’t send whole files in one go. We split files into chunks (e.g. 64 KB each). The **receiver** (the peer that’s downloading) will send one `ChunkRequest` per chunk. The **sender** (the peer that has the file) will respond with a `ChunkResponse` for that chunk. So `ChunkRequest` is the “please send chunk N of file F” message that drives the whole transfer.

---

## 3. `protocol/ChunkResponse.java` — “Here Is That Chunk”

**What it does:**  
A small object that means: “Here are the bytes for chunk number X (and the file has this many chunks in total).”

**The code in simple terms:**
- **chunkIndex:** Which chunk this is (0-based), so the receiver knows where to write it.
- **data:** The actual bytes for this chunk. The last chunk may be shorter than `CHUNK_SIZE`.
- **totalChunks:** How many chunks the file has in total (for progress like “chunk 5 of 20”).
- **Constructor and getters:** So we can build a response after reading from disk and read its fields when we serialize/deserialize.

**Why it matters for the project:**  
After the sender gets a `ChunkRequest`, it reads that chunk from the file (using `ChunkedFileReader` later) and sends a `ChunkResponse`. The receiver will take `chunkIndex` and `data` and write them to the right place in the output file (using `ChunkedFileWriter` later). So `ChunkResponse` is the “here’s the data for that chunk” message that completes one step of the transfer.

---

## 4. `protocol/MessageSerializer.java` — Same Language on Both Sides

**What it does:**  
Turns `ChunkRequest` and `ChunkResponse` into bytes on the socket and turns those bytes back into Java objects. Both peers use the same format so they understand each other.

**The code in simple terms:**
- **Wire format:** Each message starts with a **type byte** (1 = request, 2 = response). Then we write the fields in a fixed order (e.g. for a request: fileId as UTF string, then chunkIndex as 4 bytes; for a response: chunkIndex, length of data, raw bytes, totalChunks). We use `DataOutputStream` / `DataInputStream` so lengths and numbers are written in a standard way (big-endian).
- **writeChunkRequest(out, request):** Encodes the request and writes it to the stream, then flushes so the other side can read it immediately.
- **writeChunkResponse(out, response):** Same for a response.
- **read(in):** Reads the type byte, then the rest of the message, and returns either a `ChunkRequest` or a `ChunkResponse`. The caller uses `instanceof` or a similar check to see which one it is.

**Why it matters for the project:**  
Without a defined format, one peer might send “fileId then chunkIndex” and the other might expect “chunkIndex then fileId” — and the transfer would break. `MessageSerializer` is the single place that defines how requests and responses look on the wire. Later, `OutgoingTransfer` will call `read()` to get requests and `writeChunkResponse()` to send data; `IncomingTransfer` will call `writeChunkRequest()` to ask for chunks and `read()` to get responses. So this class is the “protocol layer” that the transfer layer will use to talk over the network.

---

## How This Builds Toward the Full Project

| We built        | So that later we can …                                      |
|-----------------|-------------------------------------------------------------|
| SocketConnection | Use one pattern for every connection and never leak sockets. |
| ChunkRequest     | Receiver asks for “chunk N of file F” in a clear way.      |
| ChunkResponse    | Sender replies with “here is chunk N and total count.”      |
| MessageSerializer| Send and receive these messages the same way on both peers. |

**Next step:** Implement the **file** package (`FileMetadata`, `ChunkedFileReader`, `ChunkedFileWriter`) so we can read a file by chunk index and write chunks to a file. Then **transfer** will use the protocol we just built plus those readers/writers to actually move file data over the socket.
