package com.peerdrop.transfer;

import com.peerdrop.file.ChunkedFileWriter;
import com.peerdrop.file.FileMetadata;
import com.peerdrop.protocol.ChunkRequest;
import com.peerdrop.protocol.ChunkResponse;
import com.peerdrop.protocol.MessageSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Handles receiving one file from a remote peer over an existing connection.
 *
 * Responsibilities:
 * - Run on the side of the peer that requested the file (the "client" side of the transfer).
 * - Know the file metadata (size, name) and local destination path.
 * - Create ChunkedFileWriter, then loop: send ChunkRequest(0), ChunkRequest(1), ... until all chunks received.
 * - For each ChunkResponse: write chunk via ChunkedFileWriter, then request next chunk.
 * - When all chunks received, close ChunkedFileWriter and connection (or leave connection open per protocol).
 *
 * One IncomingTransfer per file download; typically run in a separate thread so the caller can track progress.
 */
public class IncomingTransfer {

    // TODO: InputStream, OutputStream, FileMetadata, Path destination
    // TODO: run() loop: for chunkIndex 0..totalChunks-1: write ChunkRequest -> read ChunkResponse -> writeChunk
}
