package org.hyperion.rs2.net.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.Packet.Type;

import java.util.logging.Logger;

public class RS2Decoder extends CumulativeProtocolDecoder {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(RS2Decoder.class.getName());

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        /*
         * Fetch any cached opcodes and sizes, reset to -1 if not present.
         */
        int opcode = (Integer) session.getAttribute("opcode", -1);
        int size = (Integer) session.getAttribute("size", -1);

        /*
         * If the opcode is not present.
         */
        if (opcode == -1) {
            /*
             * Check if it can be read.
             */
            if (in.remaining() >= 1) {
                /*
                 * Read and decrypt the opcode.
                 */
                opcode = in.get() & 0xFF;

                /*
                 * Find the packet size.
                 */
                size = Constants.PACKET_LENGTHS[opcode];

                /*
                 * Set the cached opcode and size.
                 */
                session.setAttribute("opcode", opcode);
                session.setAttribute("size", size);
            } else {
                /*
                 * We need to wait for more data.
                 */
                return false;
            }
        }

        /*
         * If the packet is variable-length.
         */
        if (size == -1) {
            /*
             * Check if the size can be read.
             */
            if (in.remaining() >= 1) {
                /*
                 * Read the packet size and cache it.
                 */
                size = in.get() & 0xFF;
                session.setAttribute("size", size);
            } else {
                /*
                 * We need to wait for more data.
                 */
                return false;
            }
        }

        /*
         * If the packet has no value.
         */
        if (size < 0) {
            size = in.remaining();
            logger.warning("Unkown length: " + opcode + ", guessed to be: "
                    + size + ".");
        }

        /*
         * If the packet payload (data) can be read.
         */
        if (in.remaining() >= size) {
            /*
             * Read it.
             */
            final byte[] data = new byte[size];
            in.get(data);
            final IoBuffer payload = IoBuffer.allocate(data.length);
            payload.put(data);
            payload.flip();

            /*
             * Produce and write the packet object.
             */
            out.write(new Packet(opcode, Type.FIXED, payload));

            /*
             * Reset the cached opcode and sizes.
             */
            session.setAttribute("opcode", -1);
            session.setAttribute("size", -1);

            /*
             * Indicate we are ready to read another packet.
             */
            return true;
        }

        /*
         * We need to wait for more data.
         */
        return false;
    }
}
