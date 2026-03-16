package com.peerdrop.peer;

import com.peerdrop.network.SocketConnection;
import com.peerdrop.transfer.OutgoingTransfer;

import java.io.IOException;
import java.net.Socket;

/**
 * Handles one accepted incoming connection (one client connected to us).
 *
 * Run in a separate thread so the server's accept loop is not blocked.
 *
 * Responsibilities:
 * - Receive the Socket (or SocketConnection) from PeerServer.
 * - Read the first message(s) to determine what the client wants (e.g. "list files" or "give me file X, chunk N").
 * - If file transfer: create OutgoingTransfer with the socket streams and the file (from LocalFileRegistry),
 *   then run OutgoingTransfer (or let it run in this thread).
 * - When done or on error, close the SocketConnection.
 */
public class ConnectionHandler implements Runnable {

    private final Socket socket;
    // TODO: inject LocalFileRegistry (or way to resolve file and create ChunkedFileReader)

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // TODO: wrap socket in SocketConnection, parse first message, dispatch to list-files or OutgoingTransfer, then close
    }
}
