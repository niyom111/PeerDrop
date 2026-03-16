# PeerDrop – Peer-to-Peer File Sharing over LAN

PeerDrop is a peer-to-peer (P2P) file sharing application built in Java using TCP sockets and multithreading. It allows devices on the same local network to discover each other and transfer files directly without relying on a central server.

The system is designed to support large file transfers efficiently using buffered streaming and concurrent connections.

---

## Key Features

* Peer-to-peer architecture (no central file server)
* Automatic peer discovery on the LAN
* Direct file transfer between peers
* Efficient buffered file streaming
* Multithreaded connection handling
* Support for large file transfers
* Modular architecture for networking, file management, and transfer logic

---

## How It Works

Each running instance of PeerDrop acts as both a client and a server.

* As a server, the peer listens for incoming connections from other peers.
* As a client, the peer can initiate connections to request or send files.

A typical transfer flow looks like this:

1. Peer discovery finds other peers on the network.
2. A peer initiates a TCP connection to another peer.
3. A file offer or request message is sent.
4. The receiving peer accepts the transfer.
5. The file is streamed in buffered chunks over the socket connection.
6. The connection is closed once the transfer is complete.

---

## Project Architecture

```
src/main/java/com/peerdrop

network/
    SocketConnection.java
    ConnectionHandler.java

peer/
    Peer.java
    PeerServer.java
    PeerClient.java

transfer/
    FileSender.java
    FileReceiver.java
    TransferManager.java

protocol/
    Message.java
    MessageType.java

file/
    FileManager.java

discovery/
    DiscoveryService.java
    BroadcastDiscovery.java
```

### Module Overview

**network**
Handles low-level networking such as sockets, streams, and connection management.

**peer**
Represents a running peer and manages both the server and client components.

**transfer**
Implements file transfer logic including sending and receiving files.

**protocol**
Defines the communication messages exchanged between peers.

**file**
Manages file operations such as reading and writing files.

**discovery**
Handles peer discovery on the local network using broadcast messages.

---

## Technologies Used

* Java
* TCP Sockets
* Multithreading
* Buffered Streams
* Maven

---

## Building the Project

Build the project using Maven:

```
mvn clean package
```

This will compile the project and generate a runnable JAR file.

---

## Running the Application

After building:

```
java -jar target/peerdrop.jar
```

Each running instance becomes a peer capable of discovering others and transferring files.

---

## Core Concepts

**Peer**
A device running PeerDrop that can both send and receive files.

**Socket**
The network endpoint used to establish a connection between two peers.

**Stream**
A continuous flow of data through a socket used to send or receive bytes.

**Buffer**
A small memory block used to transfer file data in chunks instead of loading the entire file into memory.

---

## Future Improvements

* Parallel chunk-based file transfers
* Resume interrupted downloads
* File integrity verification (hashing)
* Encryption for secure transfers
* Simple GUI interface

---

## License

This project is licensed under the MIT License.
