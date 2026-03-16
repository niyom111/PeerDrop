# PeerDrop — Architecture & Concepts

This document explains how PeerDrop is designed and the concepts you need to understand before implementing each part.

---

## 1. Server vs Peer Architecture

### Traditional client–server
- **Server**: One process listens on a port; many clients connect to it. The server holds the files and sends them.
- **Client**: Connects to the server and downloads. Clients do not accept incoming connections.

### Peer-to-peer (our model)
- **Every node is both a server and a client**:
  - **As server**: Listens on a port for incoming connections from other peers (they can request files from us).
  - **As client**: Connects to other peers to request and download their files.
- There is no single “file server”; each peer serves its own shared folder and can request files from others.
- Optional: a small **discovery** mechanism (e.g. UDP broadcast or a lightweight registry) so peers can find each other on the LAN. This is for discovery only, not for storing or relaying file data.

**In PeerDrop**: The `Peer` class represents one running peer. It owns a `PeerServer` (accept connections) and a `PeerClient` (start connections to others). Discovery is handled by `DiscoveryService` / `BroadcastDiscovery`.

---

## 2. Socket Lifecycle

A **socket** is one end of a TCP connection. Understanding its lifecycle avoids leaks and “connection reset” errors.

1. **Create**  
   - Server: `ServerSocket(port)` then `accept()` blocks until a client connects.  
   - Client: `new Socket(host, port)` to connect to a server.

2. **Use**  
   - Get `InputStream` and `OutputStream` (or `DataInputStream` / `DataOutputStream` for structured I/O).  
   - Read/write in a defined **protocol** (e.g. message type + length + payload).  
   - Prefer **buffered** streams and a fixed **buffer size** so you don’t load entire files into memory.

3. **Close**  
   - Close the socket’s streams, then the socket.  
   - Best practice: use try-with-resources or try/finally so sockets are always closed, even on errors.  
   - If one side closes, the other will see EOF or an exception on the next read/write.

**In PeerDrop**: `SocketConnection` wraps a `Socket` and exposes streams with a consistent buffer size. `ConnectionHandler` and transfer classes use one socket per connection and must close it when the transfer or request is done.

---

## 3. Threading Model

- **Main thread**: Starts the peer (discovery, server, maybe CLI or UI).  
- **Server thread**: One thread runs `ServerSocket.accept()` in a loop. For each accepted socket, it hands off to a worker.  
- **Worker threads**: Each incoming connection is handled in a separate thread (e.g. `ConnectionHandler` in a thread pool or new thread) so one slow transfer doesn’t block others.  
- **Transfer threads**: When we **initiate** a download, we can run the read-from-socket and write-to-disk in one thread per transfer, or split read/write; either way, multiple transfers run in parallel.  
- **Discovery thread**: Optional dedicated thread for sending/receiving UDP discovery packets so it doesn’t block the server or transfers.

**Rules of thumb**:
- Never block the server’s accept loop with long I/O; always delegate to another thread.
- Use a bounded thread pool or limit concurrent transfers to avoid overwhelming the LAN or the disk.
- Shared state (e.g. list of active transfers, registered files) must be accessed in a thread-safe way (synchronized, locks, or concurrent collections).

**In PeerDrop**: `PeerServer` runs the accept loop and spawns `ConnectionHandler` (each in its own thread or pool). `PeerClient` starts transfers that run in separate threads. `TransferManager` coordinates and limits concurrency.

---

## 4. How Files Are Chunked and Transferred

Transferring whole files in memory is not an option for multi-GB files. We use **chunked streaming**.

### Chunking
- A file is split into **fixed-size chunks** (e.g. 64 KB or 1 MB). Only the last chunk may be smaller.
- **Chunk index**: 0-based. Total number of chunks = `ceil(fileSize / chunkSize)`.
- We send **metadata** first (filename, total size, chunk size, optional hash), then the requester asks for chunks by index (e.g. “file X, chunk 5”) and the sender streams only that chunk.

### Why chunk?
- **Memory**: We only buffer one chunk (or a small number) in memory.  
- **Resumability**: Later you can support “request chunk 5 only” to resume after a disconnect.  
- **Parallelism**: A receiver could request chunks 0, 1, 2, 3 from the same peer in parallel over multiple connections (advanced).

### Transfer flow (simple version)
1. **Receiver** connects to peer and sends a request: “I want file X” or “I want file X, chunk N”.  
2. **Sender** reads that chunk from disk (via `ChunkedFileReader`), writes it to the socket.  
3. **Receiver** reads from socket into a buffer, writes to disk (via `ChunkedFileWriter`) or to a temp file.  
4. Repeat for next chunk until all chunks received.  
5. Close connection (or reuse for next chunk, depending on protocol).

### Buffering
- Use a **fixed buffer** (e.g. 8 KB–64 KB) for socket read/write.  
- Read from socket into buffer → write buffer to file; never hold the whole file in memory.  
- `InputStream`/`OutputStream` should be wrapped in `BufferedInputStream`/`BufferedOutputStream` with a chosen buffer size for better throughput.

**In PeerDrop**: `ProtocolConstants` defines chunk size. `ChunkedFileReader` reads a file by chunk index; `ChunkedFileWriter` writes chunks in order. `ChunkRequest`/`ChunkResponse` (or similar) carry chunk index and data. `OutgoingTransfer`/`IncomingTransfer` orchestrate one file send/receive using these pieces.

---

## 5. High-Level Data Flow

```
[Peer A]                          [Peer B]
   |                                  |
   |  UDP broadcast "I'm here"        |
   |  ------------------------------->|  (discovery)
   |  "Here are my files"             |
   |  <-------------------------------|
   |                                  |
   |  TCP connect to B:port           |
   |  ------------------------------->|
   |  Request: file X, chunk 0        |
   |  ------------------------------->|
   |  Chunk 0 bytes                   |
   |  <-------------------------------|
   |  Request: file X, chunk 1        |
   |  ------------------------------->|
   |  ...                             |
   |  Close connection                |
   |  <------------------------------>|
```

---

## 6. Package Overview

| Package       | Purpose |
|---------------|---------|
| `com.peerdrop` | Entry point (`PeerDropMain`). |
| `discovery`   | Finding peers on the LAN (UDP broadcast, `PeerInfo`). |
| `protocol`    | Message types, constants, serialization (chunk request/response, file list). |
| `file`        | File metadata, reading/writing by chunk, list of shared files. |
| `transfer`    | Orchestrating one send or receive (outgoing/incoming transfer, chunk send/receive). |
| `peer`        | Peer lifecycle: server (accept), client (connect), connection handler. |
| `network`     | Socket wrapper with streams and buffer size. |

You will implement each layer so that:
- **network** and **protocol** are used by **transfer** and **peer**.
- **file** is used by **transfer** and by the peer when registering shared files.
- **discovery** is used by the **peer** to get a list of other peers.

Start with `network` and `protocol`, then `file`, then `transfer`, then `peer` and discovery. This keeps dependencies clear and makes testing easier.
