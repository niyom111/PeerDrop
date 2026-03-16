package com.peerdrop.peer;

import com.peerdrop.discovery.DiscoveryService;
import com.peerdrop.file.LocalFileRegistry;
import com.peerdrop.transfer.TransferManager;

import java.nio.file.Path;

/**
 * The main peer component: this process running as one node in the P2P network.
 *
 * Responsibilities:
 * - Own and start: DiscoveryService (find other peers), PeerServer (accept incoming connections),
 *   LocalFileRegistry (what we share), TransferManager (orchestrate downloads).
 * - Optionally own PeerClient (connect to others to request files) or fold that into TransferManager.
 * - start(Path sharedFolder, int port): start discovery, register shared folder, start server listening on port.
 * - stop(): stop discovery, stop server (close ServerSocket and worker threads), cancel active transfers.
 *
 * No UI here — only lifecycle. CLI or GUI can call start/stop and use getKnownPeers() / getFiles() / startDownload().
 */
public class Peer {

    // TODO: DiscoveryService, PeerServer, LocalFileRegistry, TransferManager, PeerClient (optional)
    // TODO: start(Path sharedFolder, int port), stop()
    // TODO: getters for discovery list, file list, so UI can display them
}
