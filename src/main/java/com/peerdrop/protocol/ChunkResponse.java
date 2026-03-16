package com.peerdrop.protocol;

/**
 * Message type: response containing one chunk of file data.
 *
 * Holds:
 * - Chunk index (0-based), so the receiver can write to the correct position.
 * - Chunk data (byte array or ByteBuffer). Length may be less than CHUNK_SIZE for the last chunk.
 * - Optional: total chunks or file size for progress.
 *
 * The receiver uses ChunkedFileWriter to write this chunk at the right offset.
 */
public class ChunkResponse {

    // TODO: fields (chunkIndex, byte[] data, optional totalChunks)
    // TODO: constructor, getters
}
