package com.peerdrop.file;

import com.peerdrop.protocol.ProtocolConstants;

/**
 * Metadata for one shared file: name, size, and derived chunk count.
 *
 * Used by:
 * - LocalFileRegistry to describe what we share.
 * - Protocol messages (file list, chunk request/response) to identify the file and compute chunk indices.
 */
public class FileMetadata {

    private final String fileId;
    private final String fileName;
    private final long fileSize;

    public FileMetadata(String fileId, String fileName, long fileSize) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    /**
     * Total number of chunks for this file (0-based chunk indices 0 .. getChunkCount()-1).
     */
    public int getChunkCount() {
        return (int) Math.ceil((double) fileSize / ProtocolConstants.CHUNK_SIZE);
    }
}
