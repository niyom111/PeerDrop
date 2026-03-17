package com.peerdrop.protocol;

public class ChunkRequest {

    private final String fileId;  //which file we want to download
    private final int chunkIndex; //chunk of the file we want

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
