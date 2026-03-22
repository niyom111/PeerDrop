package com.peerdrop.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

    private volatile Path sharedRoot;
    private final ConcurrentHashMap<String, FileMetadata> fileIdToMetadata = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Path> fileIdToPath = new ConcurrentHashMap<>();

    /**
     * Sets the shared root and scans it (single level) for regular files, registering each with a file id
     * (relative path string) and metadata.
     *
     * @param sharedRoot root directory to share (must be an existing directory)
     * @throws IOException if the directory cannot be read
     */
    public void add(Path sharedRoot) throws IOException {
        if (!Files.isDirectory(sharedRoot)) {
            throw new IOException("Shared root is not a directory: " + sharedRoot);
        }
        this.sharedRoot = sharedRoot.toAbsolutePath().normalize();
        fileIdToMetadata.clear();
        fileIdToPath.clear();
        try (var stream = Files.list(sharedRoot)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                String fileId = sharedRoot.relativize(path.toAbsolutePath().normalize()).toString();
                String fileName = path.getFileName().toString();
                long size;
                try {
                    size = Files.size(path);
                } catch (IOException e) {
                    return;
                }
                FileMetadata meta = new FileMetadata(fileId, fileName, size);
                fileIdToMetadata.put(fileId, meta);
                fileIdToPath.put(fileId, path.toAbsolutePath().normalize());
            });
        }
    }

    /**
     * Returns a snapshot of all registered file metadata.
     */
    public List<FileMetadata> getFiles() {
        return new ArrayList<>(fileIdToMetadata.values());
    }

    /**
     * Resolves a file id to the absolute Path for that file, if it is registered.
     * Validates that the resolved path is under the shared root to avoid path traversal.
     *
     * @param fileId logical file id (e.g. relative path from shared root)
     * @return the Path for that file, or null if not registered or invalid
     */
    public Path resolvePath(String fileId) {
        Path root = sharedRoot;
        if (root == null) {
            return null;
        }
        if (!fileIdToPath.containsKey(fileId)) {
            return null;
        }
        Path resolved = root.resolve(fileId).normalize().toAbsolutePath();
        if (!resolved.startsWith(root)) {
            return null;
        }
        return fileIdToPath.get(fileId);
    }
}
