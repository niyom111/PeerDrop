package com.peerdrop.discovery;

/**
 * Represents another peer on the LAN that we discovered.
 *
 * Holds:
 * - Host (IP or hostname) and port to connect to for TCP transfers.
 * - Optional: display name, peer id, list of shared file names (if sent during discovery).
 *
 * Used by PeerClient to know where to connect when the user requests a file from this peer.
 */
public class PeerInfo {

    // TODO: fields (host, port, name, id, etc.)
    // TODO: constructor, getters
}
