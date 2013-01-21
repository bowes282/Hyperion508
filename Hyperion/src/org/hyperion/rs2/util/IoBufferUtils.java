package org.hyperion.rs2.util;

import org.apache.mina.core.buffer.IoBuffer;

public final class IoBufferUtils {

    /**
     * Writes a string
     *
     * @param buffer The ChannelBuffer
     * @param string The string being wrote.
     */
    public static void putJagString(IoBuffer buffer, String string) {
        buffer.put((byte) 0);
        buffer.put(string.getBytes());
        buffer.put((byte) 0);
    }

    /**
     * Writes a smart
     *
     * @param buffer The ChannelBuffer
     * @param value  The value being wrote
     */
    public static void putSmart(IoBuffer buffer, int value) {
        if ((value ^ 0xffffffff) > -129) {
            buffer.put((byte) value);
        } else {
            buffer.putShort((short) value);
        }
    }

    /**
     * Reads a RuneScape string from a buffer.
     *
     * @param buf The buffer.
     * @return The string.
     */
    public static String getRS2String(IoBuffer buf) {
        final StringBuilder bldr = new StringBuilder();
        byte b;
        while (buf.hasRemaining() && (b = buf.get()) != 0) {
            bldr.append((char) b);
        }
        return bldr.toString();
    }

    /**
     * Writes a RuneScape string to a buffer.
     *
     * @param buf    The buffer.
     * @param string The string.
     */
    public static void putRS2String(IoBuffer buf, String string) {
        for (final char c : string.toCharArray()) {
            buf.put((byte) c);
        }
        buf.put((byte) 0);
    }
}
