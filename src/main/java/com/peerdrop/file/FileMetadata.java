package com.peerdrop.file;

/**
 * Metadata for one shared file: name, size, and derived chunk count.
 *
 * Used by:
 * - LocalFileRegistry to describe what we share.
 * - Protocol messages (file list, chunk request/response) to identify the file and compute chunk indices.
 */
public class FileMetadata {

    // TODO: fields (fileName or path, long fileSize)
    // TODO: constructor, getters
    // TODO: method to compute chunk count: (int) Math.ceil((double) fileSize / ProtocolConstants.CHUNK_SIZE)
}
