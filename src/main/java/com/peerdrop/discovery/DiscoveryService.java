package com.peerdrop.discovery;

import java.util.List;

/**
 * Abstraction for discovering peers on the LAN.
 *
 * Implementations might use:
 * - UDP broadcast: send "I'm here" and listen for others.
 * - A small registry server: peers register and fetch the list.
 *
 * Responsibilities:
 * - start(): begin listening/broadcasting (often on a dedicated thread).
 * - stop(): stop discovery and release resources (e.g. close DatagramSocket).
 * - getKnownPeers(): return current list of PeerInfo (may change over time).
 *
 * The Peer uses this to populate a list of peers the user can choose from.
 */
public interface DiscoveryService {

    void start();

    void stop();

    List<PeerInfo> getKnownPeers();
}
