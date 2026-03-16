# PeerDrop

A **peer-to-peer LAN file sharing** learning project in Java. Goals: Java sockets, large file transfers (multi-GB), multithreading, streaming with buffers, and high LAN throughput.

---

## Learning path

1. Read **[ARCHITECTURE.md](ARCHITECTURE.md)** for:
   - Server vs peer architecture
   - Socket lifecycle
   - Threading model
   - How files are chunked and transferred

2. Implement in this order (dependencies first):
   - **network** → **protocol** → **file** → **transfer** → **peer** → **discovery** → **main**

3. Run: `mvn compile` then `mvn exec:java` (or add exec plugin and run `PeerDropMain`).

---

## Folder and class purpose

### Root
- **pom.xml** — Maven build (Java 17, jar with main class).
- **ARCHITECTURE.md** — Design and concepts (read this first).
- **README.md** — This file.

### `src/main/java/com/peerdrop/`
- **PeerDropMain** — Entry point: parse args, create and start `Peer`, shutdown hook.

### `com.peerdrop.discovery`
- **DiscoveryService** — Interface: start/stop discovery, return list of known peers.
- **BroadcastDiscovery** — UDP broadcast implementation to find peers on LAN.
- **PeerInfo** — One discovered peer: host, port, optional name/id.

### `com.peerdrop.protocol`
- **ProtocolConstants** — Chunk size, socket buffer size, ports (single place to tune).
- **Message** — One protocol message (type + payload).
- **MessageSerializer** — Encode/decode messages to/from streams.
- **ChunkRequest** — Request: “send me file X, chunk N”.
- **ChunkResponse** — Response: chunk index + chunk bytes.
- **FileListingMessage** — List of shared files (metadata) to send to other peers.

### `com.peerdrop.file`
- **FileMetadata** — Name, size, chunk count for one file.
- **ChunkedFileReader** — Read a file by chunk index (no full-file load).
- **ChunkedFileWriter** — Write a file by chunk index (stream to disk).
- **LocalFileRegistry** — Scan shared folder, list files, resolve file id → path for serving.

### `com.peerdrop.transfer`
- **TransferManager** — Start downloads, optionally limit concurrency.
- **OutgoingTransfer** — Serve one file: read ChunkRequests, send ChunkResponses (we have the file).
- **IncomingTransfer** — Receive one file: send ChunkRequests, write ChunkResponses to disk (we request the file).

### `com.peerdrop.peer`
- **Peer** — Main peer: owns discovery, server, registry, transfer manager; start/stop.
- **PeerServer** — Listen on port, accept connections, hand each to ConnectionHandler.
- **PeerClient** — Connect to a PeerInfo, return SocketConnection for transfer.
- **ConnectionHandler** — Handle one incoming connection (run in thread): interpret protocol, run OutgoingTransfer or send file list.

### `com.peerdrop.network`
- **SocketConnection** — Wraps Socket with buffered streams of protocol buffer size; close safely.

---

## Build and run

```bash
mvn compile
mvn exec:java -q
```

(Add `exec-maven-plugin` in `pom.xml` with `mainClass` for `mvn exec:java` if needed; the jar plugin already sets the main class for `java -jar`.)

---

## Suggested implementation order

1. **ProtocolConstants** — Already has constants.
2. **SocketConnection** — Implement `close()`; rest is done.
3. **Message types** — ChunkRequest, ChunkResponse, FileListingMessage; then Message + MessageSerializer.
4. **FileMetadata** — Fields + `getChunkCount()` using `ProtocolConstants.CHUNK_SIZE`.
5. **ChunkedFileReader** / **ChunkedFileWriter** — One chunk at a time, fixed buffer.
6. **LocalFileRegistry** — Scan directory, store metadata and path.
7. **OutgoingTransfer** / **IncomingTransfer** — Request/response loop with MessageSerializer.
8. **ConnectionHandler** — Parse first message, call OutgoingTransfer or send file list.
9. **PeerServer** — Accept loop + executor, spawn ConnectionHandler.
10. **PeerClient** — Connect to PeerInfo, return SocketConnection.
11. **TransferManager** — Start IncomingTransfer using PeerClient.
12. **PeerInfo** — Fields + getters.
13. **BroadcastDiscovery** — UDP send/receive, update list of PeerInfo.
14. **Peer** — Wire everything: start discovery, server, registry; stop all.
15. **PeerDropMain** — Parse args, create Peer, start, shutdown hook.

You can add a simple CLI (e.g. “list peers”, “list files”, “download &lt;peer&gt; &lt;file&gt;”) or a minimal UI once the core works.
