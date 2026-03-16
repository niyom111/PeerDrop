package com.peerdrop.protocol;

/**
 * Represents a single message in the PeerDrop protocol.
 *
 * Messages can be:
 * - Request: e.g. "send me file X" or "send me file X, chunk N".
 * - Response: e.g. "here is chunk N" (with payload) or "here is file metadata".
 * - Control: e.g. "list your files", "goodbye".
 *
 * This class (or a hierarchy) holds message type and typed payload so that
 * MessageSerializer can write/read a consistent format (e.g. type byte + length + payload).
 */
public class Message {

    // TODO: define message types (enum or constants), fields for type + payload
    // TODO: constructors and getters
}
