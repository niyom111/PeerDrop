package com.peerdrop.file;

import com.peerdrop.protocol.ProtocolConstants;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;


public class ChunkedFileWriter {

    private Path path;
    private RandomAccessFile raf;

    public void open(Path path, long totalSize) throws IOException {
        if (raf != null) {
            close();
        }
        this.path = path;
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        this.raf = new RandomAccessFile(path.toFile(), "rw");
        this.raf.setLength(totalSize);  //totalsize of the file in bytes
    }

    /**
     * Writes a chunk at the given 0-based index.
     *
     * @param chunkIndex 0-based chunk index
     * @param data       buffer containing chunk bytes
     * @param length     number of bytes to write (last chunk may be smaller than CHUNK_SIZE)
     * @throws IOException if write fails
     */
    public void writeChunk(int chunkIndex, byte[] data, int length) throws IOException {
        if (raf == null) {
            throw new IOException("Writer not open");
        }
        long offset = (long) chunkIndex * ProtocolConstants.CHUNK_SIZE;
        raf.seek(offset);
        raf.write(data, 0, length);  //0 as always we want to write from the beginning of the particular chunk. 
    }

    //fd: file descriptor is a unique identifier to the file. low lvel connection to actual file
    public void close() throws IOException {
        if (raf != null) {
            try {
                raf.getFD().sync();  //os buffer to actual disk. os buffer is different from socket buffer
            } catch (IOException ignored) {
            }
            raf.close();
            raf = null;
        }
    }
}
