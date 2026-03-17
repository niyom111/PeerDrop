package com.peerdrop.protocol;

/**
 * Message type: response containing one chunk of file data.
 *
 * Holds:
 * - Chunk index (0-based), so the receiver can write to the correct position.
 * - Chunk data (byte array). Length may be less than CHUNK_SIZE for the last chunk.
 * - Optional: total chunks for progress.
 *
 * The receiver uses ChunkedFileWriter to write this chunk at the right offset.
 */
public class ChunkResponse {

    private final int chunkIndex;
    private final byte[] data;
    private final int totalChunks;

    public ChunkResponse(int chunkIndex, byte[] data, int totalChunks) {
        this.chunkIndex = chunkIndex;
        this.data = data;
        this.totalChunks = totalChunks;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public byte[] getData() {
        return data;
    }

    public int getTotalChunks() {
        return totalChunks;
    }
}
