package com.peerdrop.network;

import com.peerdrop.protocol.ProtocolConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SocketConnection implements AutoCloseable {  //class must have a close method. so it will automatically call a close method.

    private final Socket socket;  //after final keyword , cannot reinitialize.

    public SocketConnection(Socket socket) {
        this.socket = socket;
    }

    public InputStream getInputStream() throws IOException { //may get an error in input output. we need to handle it
        return new BufferedInputStream(socket.getInputStream(), ProtocolConstants.SOCKET_BUFFER_SIZE);
    }

    public OutputStream getOutputStream() throws IOException {
        return new BufferedOutputStream(socket.getOutputStream(), ProtocolConstants.SOCKET_BUFFER_SIZE);
    }

    @Override
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
}
