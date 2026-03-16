package com.peerdrop;

/**
 * Entry point for PeerDrop.
 *
 * Responsibilities:
 * - Parse command-line args (e.g. shared folder path, port).
 * - Create and start the Peer (which starts discovery, server, and optional client).
 * - Shutdown hook to close the peer gracefully (close server socket, stop discovery).
 *
 * No file transfer logic here — only bootstrap and lifecycle.
 */
public class PeerDropMain {

    public static void main(String[] args) {
        // TODO: parse args, create Peer, start it, register shutdown hook
    }
}
