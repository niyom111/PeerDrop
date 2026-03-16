package com.peerdrop.transfer;

import com.peerdrop.file.ChunkedFileReader;
import com.peerdrop.file.FileMetadata;
import com.peerdrop.protocol.ChunkRequest;
import com.peerdrop.protocol.ChunkResponse;
import com.peerdrop.protocol.MessageSerializer;
import com.peerdrop.protocol.ProtocolConstants;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Handles sending one file to a remote peer over an existing connection.
 *
 * Responsibilities:
 * - Run on the side of the peer that has the file (the "server" side of the transfer).
 * - Read ChunkRequest messages from the InputStream (using MessageSerializer).
 * - For each request: use ChunkedFileReader to read that chunk, build ChunkResponse, write to OutputStream.
 * - When the client closes or sends "done", release ChunkedFileReader and close resources.
 *
 * One OutgoingTransfer per file transfer; the connection may be closed after the transfer or reused (protocol choice).
 */
public class OutgoingTransfer {

    // TODO: InputStream, OutputStream, ChunkedFileReader (or Path + FileMetadata to create reader)
    // TODO: run() loop: read ChunkRequest -> readChunk(index) -> write ChunkResponse
}
