package com.peerdrop.file;

import java.nio.file.Path;
import java.util.List;

/**
 * Registry of files this peer is currently sharing (from a shared folder).
 *
 * Responsibilities:
 * - add(Path sharedRoot): scan the folder (and optionally subfolders) and register each file with its metadata
 *   and a logical id or path relative to shared root (so we can resolve "file X" to a real Path and ChunkedFileReader).
 * - getFiles(): return list of FileMetadata for UI or for sending a file list to other peers.
 * - resolvePath(String fileId): return the real Path for a given file id (for serving a chunk request).
 *
 * Thread-safe if multiple threads ask for file list or resolve path while the registry is updated.
 */
public class LocalFileRegistry {

    // TODO: shared root Path, list or map of (id -> Path or FileMetadata)
    // TODO: add(Path sharedRoot), getFiles(), resolvePath(String)
}
