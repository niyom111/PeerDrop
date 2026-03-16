package com.peerdrop.protocol;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializes and deserializes protocol messages to/from the wire.
 *
 * Responsibilities:
 * - write(Message, OutputStream): encode the message (e.g. type, length, payload) and write to stream.
 * - read(InputStream): read bytes from stream and decode into a Message (block until one full message).
 *
 * Define a simple binary format, e.g.:
 * - 1 byte: message type
 * - 4 bytes: payload length (big-endian)
 * - N bytes: payload (for chunk response: chunk index + chunk data; for request: file id + chunk index; etc.)
 *
 * This keeps the rest of the code independent of the exact wire format.
 */
public final class MessageSerializer {

    // TODO: write(Message msg, OutputStream out)
    // TODO: read(InputStream in) -> Message
}
