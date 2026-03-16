package com.peerdrop.peer;

import com.peerdrop.discovery.PeerInfo;
import com.peerdrop.network.SocketConnection;

import java.io.IOException;

/**
 * Client role: connect to another peer to request files.
 *
 * Responsibilities:
 * - connect(PeerInfo peer): open a TCP Socket to peer.getHost() and peer.getPort(), wrap in SocketConnection.
 *   Return the SocketConnection so the caller (e.g. TransferManager or IncomingTransfer) can send ChunkRequests
 *   and read ChunkResponses.
 * - Optional: maintain a connection pool per peer to reuse connections (advanced).
 *
 * Does not implement the transfer protocol — only establishes the connection. IncomingTransfer or TransferManager
 * use this to get a SocketConnection, then run the request/response loop.
 */
public class PeerClient {

    // TODO: connect(PeerInfo) -> SocketConnection (or Socket) for the caller to use
}
