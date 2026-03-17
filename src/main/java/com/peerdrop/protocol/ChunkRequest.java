package com.peerdrop.protocol;

/**
 * Message type: request a specific chunk of a file from a peer.
 *
 * Holds:
 * - File identifier (e.g. path relative to shared root, or a file id agreed in a prior "file list" message).
 * - Chunk index (0-based).
 *
 * The sender will respond with a ChunkResponse containing the chunk data (or an error).
 */
public class ChunkRequest {

    private final String fileId;
    private final int chunkIndex;

    public ChunkRequest(String fileId, int chunkIndex) {
        this.fileId = fileId;
        this.chunkIndex = chunkIndex;
    }

    public String getFileId() {
        return fileId;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }
}
