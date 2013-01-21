package org.hyperion.rs2.net.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.hyperion.rs2.util.IoBufferUtils;

public class RS2WorldListEncoder {

    /**
     * An Array of worlds.
     */
    private final static RS2WorldList[] worldList = new RS2WorldList[0];

    public static IoBuffer encode(boolean worldStatus,
                                  boolean worldConfiguration) {        /*
         * The channel buffer.
		 */
        final IoBuffer buffer = IoBuffer.allocate(1024);
		/*
		 * The world status writing as a byte.
		 */
        buffer.put((byte) (worldStatus ? 1 : 0));
		/*
		 * The world config writing as a byte.
		 */
        buffer.put((byte) (worldConfiguration ? 1 : 0));

		/*
		 * The world configuration data being written.
		 */
        if (worldConfiguration) {
            IoBufferUtils.putSmart(buffer, worldList.length);
            setCountry(buffer);
            IoBufferUtils.putSmart(buffer, 0);
            IoBufferUtils.putSmart(buffer, (worldList.length + 1));
            IoBufferUtils.putSmart(buffer, worldList.length);
            for (final RS2WorldList w : worldList) {
                IoBufferUtils.putSmart(buffer, w.getWorldId());
                buffer.put((byte) w.getLocation());
                buffer.putInt(w.getFlag());
                IoBufferUtils.putJagString(buffer, w.getActivity()); // activity
                IoBufferUtils.putJagString(buffer, w.getIp()); // ip // address
            }
            buffer.putInt(-626474014); // != 0
        }

		/*
		 * The status data being written.
		 */
        if (worldStatus) {
            for (final RS2WorldList w : worldList) {
                IoBufferUtils.putSmart(buffer, w.getWorldId()); // world id
                buffer.putShort((short) 5); // player count
            }
        }
		/*
		 * The final data being written to the client.
		 */
        buffer.flip();
        final IoBuffer finalBuffer = IoBuffer.allocate(buffer.limit() + 3);
        finalBuffer.put((byte) 0);
        finalBuffer.putShort((short) buffer.limit());
        finalBuffer.put(buffer);
        finalBuffer.flip();
		/*
		 * Finally we write the finalBuffer
		 */
        return finalBuffer;
    }

    /*
     * The country data being written.
     */
    private static void setCountry(IoBuffer buffer) {
        for (final RS2WorldList w : worldList) {
            IoBufferUtils.putSmart(buffer, w.getCountry());
            IoBufferUtils.putJagString(buffer, w.getRegion());
        }
    }
}
