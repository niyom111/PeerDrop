package com.peerdrop.protocol;

import java.io.DataInputStream;  
import java.io.DataOutputStream; //DataOutputStream is a helper that lets you write numbers, strings, etc. into a stream as bytes.
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


//network, files , hardware all work on raw bytes os thus we need to convert

public final class MessageSerializer {

    private static final byte TYPE_CHUNK_REQUEST = 1;
    private static final byte TYPE_CHUNK_RESPONSE = 2;

   //write: read high level data and convert it into bytes
   //out is just a pipe. doesnt containt data. i am stupid :()
   //stream is a way to write data into socket
    public static void writeChunkRequest(OutputStream out, ChunkRequest request) throws IOException {   
        DataOutputStream dataOut = new DataOutputStream(out);  //predefined function.
        dataOut.writeByte(TYPE_CHUNK_REQUEST); 
        dataOut.writeUTF(request.getFileId());  //converts string to bytes. length of the string followed by the characters.
        dataOut.writeInt(request.getChunkIndex());  //convert integer to byte
        dataOut.flush();  //forces data to ve sent immeidately else it will just be there in the buffer
    }

    public static void writeChunkResponse(OutputStream out, ChunkResponse response) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeByte(TYPE_CHUNK_RESPONSE);
        dataOut.writeInt(response.getChunkIndex());
        dataOut.writeInt(response.getData().length);
        dataOut.write(response.getData());
        dataOut.writeInt(response.getTotalChunks());
        dataOut.flush();
    }



    //read: read bytes and convert it into high level data
    public static Object read(InputStream in) throws IOException {   
        DataInputStream dataIn = new DataInputStream(in);
        byte type = dataIn.readByte();  //read the first byte. 1 or 2
        switch (type) {
            case TYPE_CHUNK_REQUEST: {
                String fileId = dataIn.readUTF();
                int chunkIndex = dataIn.readInt();
                return new ChunkRequest(fileId, chunkIndex);
            }
            case TYPE_CHUNK_RESPONSE: {
                int chunkIndex = dataIn.readInt();
                int dataLen = dataIn.readInt();
                byte[] data = new byte[dataLen];  //create memory to hold data
                dataIn.readFully(data);
                int totalChunks = dataIn.readInt();
                return new ChunkResponse(chunkIndex, data, totalChunks);
            }
            default:
                throw new IOException("Unknown message type: " + type);
        }
    }
}
