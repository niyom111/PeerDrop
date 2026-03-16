package com.peerdrop.protocol;

/**
 * Central place for protocol and buffer constants.
 *
 * Use these everywhere so chunk size and buffer sizes are consistent:
 * - CHUNK_SIZE: size of one file chunk (e.g. 64 KiB or 1 MiB). Larger = fewer round-trips, more memory per chunk.
 * - SOCKET_BUFFER_SIZE: size of the buffer used when reading/writing to sockets (e.g. 8 KiB–64 KiB).
 * - Port numbers for server and discovery (e.g. 8765 for TCP, 8766 for UDP).
 * - Optional: magic bytes or version for the protocol.
 */
public final class ProtocolConstants {

    /** Size of one file chunk in bytes. Tune for LAN throughput vs memory. */
    public static final int CHUNK_SIZE = 64 * 1024; // 64 KiB

    /** Buffer size for socket streams. Should be <= CHUNK_SIZE for simplicity. */
    public static final int SOCKET_BUFFER_SIZE = 8 * 1024; // 8 KiB

    /** Port this peer listens on for incoming TCP file-transfer connections. */
    public static final int SERVER_PORT = 8765;

    /** Port used for UDP discovery broadcast. */
    public static final int DISCOVERY_PORT = 8766;

    private ProtocolConstants() {}
}
