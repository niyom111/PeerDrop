package com.peerdrop.transfer;

import com.peerdrop.discovery.PeerInfo;
import com.peerdrop.file.FileMetadata;

/**
 * Orchestrates outgoing and incoming file transfers.
 *
 * Responsibilities:
 * - startDownload(PeerInfo peer, String fileId, FileMetadata metadata, java.nio.file.Path localDestination):
 *   initiate an outgoing transfer (connect to peer, request chunks one by one, write via ChunkedFileWriter).
 *   Run in a dedicated thread or submit to a pool so it doesn't block the caller.
 * - When another peer requests a file from us, the ConnectionHandler will ask the TransferManager (or directly
 *   use OutgoingTransfer) to serve the file; alternatively, the handler itself creates an OutgoingTransfer.
 *
 * Optional: limit concurrent transfers (e.g. semaphore or fixed thread pool), track active transfers for progress/cancel.
 */
public class TransferManager {

    // TODO: dependency on PeerClient or socket factory to connect to a peer
    // TODO: startDownload(...), maybe startUpload for the server side or that lives in ConnectionHandler
}
