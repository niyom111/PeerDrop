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

    // TODO: fields (fileId or path, chunkIndex)
    // TODO: constructor, getters; optionally implement as part of Message payload
}
