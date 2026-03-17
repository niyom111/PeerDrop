package com.peerdrop.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializes and deserializes ChunkRequest and ChunkResponse to/from the wire.
 *
 * Wire format:
 * - 1 byte: message type (1 = ChunkRequest, 2 = ChunkResponse)
 * - ChunkRequest: UTF string (fileId), 4 bytes (chunkIndex)
 * - ChunkResponse: 4 bytes (chunkIndex), 4 bytes (data length), N bytes (data), 4 bytes (totalChunks)
 */
public final class MessageSerializer {

    private static final byte TYPE_CHUNK_REQUEST = 1;
    private static final byte TYPE_CHUNK_RESPONSE = 2;

    /**
     * Writes a ChunkRequest to the stream. Flushes so the other side can read immediately.
     */
    public static void writeChunkRequest(OutputStream out, ChunkRequest request) throws IOException {   //where to send the request to(OutputStream)
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeByte(TYPE_CHUNK_REQUEST);
        dataOut.writeUTF(request.getFileId());
        dataOut.writeInt(request.getChunkIndex());
        dataOut.flush();
    }

    /**
     * Writes a ChunkResponse to the stream. Flushes so the other side can read immediately.
     */
    public static void writeChunkResponse(OutputStream out, ChunkResponse response) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeByte(TYPE_CHUNK_RESPONSE);
        dataOut.writeInt(response.getChunkIndex());
        dataOut.writeInt(response.getData().length);
        dataOut.write(response.getData());
        dataOut.writeInt(response.getTotalChunks());
        dataOut.flush();
    }

    /**
     * Reads one message from the stream. Returns either a ChunkRequest or a ChunkResponse.
     * Blocks until the full message is available. Throws IOException on stream error or EOF.
     */
    public static Object read(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        byte type = dataIn.readByte();
        switch (type) {
            case TYPE_CHUNK_REQUEST: {
                String fileId = dataIn.readUTF();
                int chunkIndex = dataIn.readInt();
                return new ChunkRequest(fileId, chunkIndex);
            }
            case TYPE_CHUNK_RESPONSE: {
                int chunkIndex = dataIn.readInt();
                int dataLen = dataIn.readInt();
                byte[] data = new byte[dataLen];
                dataIn.readFully(data);
                int totalChunks = dataIn.readInt();
                return new ChunkResponse(chunkIndex, data, totalChunks);
            }
            default:
                throw new IOException("Unknown message type: " + type);
        }
    }
}
