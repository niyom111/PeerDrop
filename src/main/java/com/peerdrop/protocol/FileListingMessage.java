package com.peerdrop.protocol;

import java.util.List;

/**
 * Message type: list of files this peer is sharing.
 *
 * Sent (e.g. in discovery or on first connection) so that other peers know what they can request.
 * Holds a list of file metadata (name, size, maybe relative path or id) — can reuse FileMetadata from the file package.
 */
public class FileListingMessage {

    // TODO: list of FileMetadata or simple (name, size) pairs
    // TODO: constructor, getters
}
