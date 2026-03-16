package com.peerdrop.file;

import com.peerdrop.protocol.ProtocolConstants;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Reads a file in fixed-size chunks without loading the whole file into memory.
 *
 * Responsibilities:
 * - open(Path path): associate with a file (e.g. open FileChannel or RandomAccessFile).
 * - readChunk(int chunkIndex): read the bytes for that chunk (chunkIndex 0-based) into a buffer and return.
 *   Last chunk may be smaller than CHUNK_SIZE.
 * - getChunkCount(): return total number of chunks (from file size and CHUNK_SIZE).
 * - close(): release file handle.
 *
 * Use a fixed buffer (size CHUNK_SIZE) and seek to offset chunkIndex * CHUNK_SIZE for each read.
 */
public class ChunkedFileReader {

    // TODO: RandomAccessFile or FileChannel, Path, buffer
    // TODO: open(Path), readChunk(int), getChunkCount(), close()
}
