package com.peerdrop.protocol;


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
