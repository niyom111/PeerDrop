package com.peerdrop.discovery;

/**
 * Discovery implementation using UDP broadcast on the LAN.
 *
 * Responsibilities:
 * - Periodically send a UDP datagram to the broadcast address (e.g. 255.255.255.255 or subnet broadcast)
 *   containing our peer info (our IP, port, maybe name).
 * - Listen for incoming broadcast packets from other peers and add them to the known-peers list.
 * - Use a dedicated thread (or two: one send, one receive) so discovery doesn't block the server.
 *
 * Protocol (conceptual): each packet could be a simple string or small binary payload, e.g.
 * "PEERDROP|host|port|name" so other peers can parse and create PeerInfo.
 */
public class BroadcastDiscovery implements DiscoveryService {

    @Override
    public void start() {
        // TODO: open DatagramSocket, start send loop and receive loop (e.g. in a thread)
    }

    @Override
    public void stop() {
        // TODO: stop threads, close DatagramSocket
    }

    @Override
    public java.util.List<PeerInfo> getKnownPeers() {
        // TODO: return thread-safe list of peers discovered via broadcast
        return null;
    }
}
