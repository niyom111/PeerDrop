package com.peerdrop.file;

import com.peerdrop.protocol.ProtocolConstants;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChunkedFileReader {

    private Path path;  //where the file is
    private RandomAccessFile raf;  //tool to read file from any position. new RandomAccessFile(file, "r")
    private int chunkCount;

    public void open(Path path) throws IOException {
        if (raf != null) {
            close();   //if another file is open, close it
        }
        this.path = path;
        long size = Files.size(path);     //Files is a predefined class
        this.chunkCount = (int) Math.ceil((double) size / ProtocolConstants.CHUNK_SIZE);
        if (this.chunkCount == 0 && size > 0) {
            this.chunkCount = 1;
        }   //redundant but for extra safety. ceil doe it for us
        this.raf = new RandomAccessFile(path.toFile(), "r");   //path is modern type.convert to file as raf uses file.
    }

    
    public byte[] readChunk(int chunkIndex) throws IOException {
        if (raf == null) {
            throw new IOException("Reader not open");
        }
        if (chunkIndex < 0 || chunkIndex >= chunkCount) {
            throw new IOException("Chunk index out of range: " + chunkIndex + " (count=" + chunkCount + ")");
        }
        long fileSize = raf.length();
        long offset = (long) chunkIndex * ProtocolConstants.CHUNK_SIZE;
        int toRead = (int) Math.min(ProtocolConstants.CHUNK_SIZE, fileSize - offset);
        byte[] buffer = new byte[toRead];
        raf.seek(offset);
        raf.readFully(buffer);   //file is already in bit format. no need for conversion. conversion for chunkrequest.java
        return buffer; 
    }

    public int getChunkCount() {
        return chunkCount;
    }


    public void close() {
        if (raf != null) {
            try {
                raf.close();
            } catch (IOException ignored) {
            }
            raf = null;
        }
    }
}

