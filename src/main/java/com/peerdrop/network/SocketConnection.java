package com.peerdrop.network;

import com.peerdrop.protocol.ProtocolConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Wraps a TCP Socket with buffered streams of a known size.
 *
 * Responsibilities:
 * - Hold the Socket and expose getInputStream() / getOutputStream() wrapped in
 *   BufferedInputStream / BufferedOutputStream with SOCKET_BUFFER_SIZE.
 * - Provide close() that closes both streams and the socket (idempotent if possible).
 *
 * Use this everywhere we do protocol I/O so buffer sizes are consistent and we don't forget to buffer.
 */
public class SocketConnection implements AutoCloseable {

    private final Socket socket;

    public SocketConnection(Socket socket) {
        this.socket = socket;
    }

    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(socket.getInputStream(), ProtocolConstants.SOCKET_BUFFER_SIZE);
    }

    public OutputStream getOutputStream() throws IOException {
        return new BufferedOutputStream(socket.getOutputStream(), ProtocolConstants.SOCKET_BUFFER_SIZE);
    }

    @Override
    public void close() {
        // TODO: close input, output, then socket; catch and log or suppress
    }
}
