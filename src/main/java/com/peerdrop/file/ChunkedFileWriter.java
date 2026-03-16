package com.peerdrop.file;

import com.peerdrop.protocol.ProtocolConstants;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Writes a file from incoming chunks in order, using a fixed buffer.
 *
 * Responsibilities:
 * - open(Path path, long totalSize): create or overwrite file, optionally pre-allocate (for progress).
 * - writeChunk(int chunkIndex, byte[] data, int length): write this chunk at offset chunkIndex * CHUNK_SIZE.
 *   Handles out-of-order chunks if you later add a buffer; for a first version, write in order.
 * - close(): flush and close the file.
 *
 * Use FileChannel or RandomAccessFile to seek and write only the chunk region — no need to load the whole file.
 */
public class ChunkedFileWriter {

    // TODO: FileChannel or RandomAccessFile, Path
    // TODO: open(Path, long totalSize), writeChunk(int, byte[], int), close()
}
