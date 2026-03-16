package com.peerdrop.peer;

import com.peerdrop.protocol.ProtocolConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Listens for incoming TCP connections from other peers (server role).
 *
 * Responsibilities:
 * - Bind ServerSocket to the configured port (e.g. ProtocolConstants.SERVER_PORT).
 * - Loop: accept() a new Socket, then hand it off to a ConnectionHandler (run in a new thread or executor).
 *   Never do long I/O in the accept loop — only accept and dispatch.
 * - stop(): close the ServerSocket (which will throw from accept() if blocked) and shut down the executor.
 *
 * Each accepted socket represents one incoming transfer or request; ConnectionHandler interprets the protocol.
 */
public class PeerServer {

    // TODO: ServerSocket, ExecutorService (e.g. Executors.newCachedThreadPool or fixed)
    // TODO: constructor(port or use constant), start(), stop()
    // TODO: need a way to create ConnectionHandler (e.g. inject LocalFileRegistry, or a factory)
}
