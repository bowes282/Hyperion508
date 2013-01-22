package org.hyperion.rs2.net.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.RS2Server;
import org.hyperion.rs2.model.PlayerDetails;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.util.IoBufferUtils;
import org.hyperion.rs2.util.NameUtils;

import java.security.SecureRandom;
import java.util.logging.Logger;

public class RS2LoginDecoder extends CumulativeProtocolDecoder {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(RS2LoginDecoder.class.getName());
    /**
     * Opcode stage.
     */
    public static final int STATE_OPCODE = 0;
    /**
     * Login stage.
     */
    public static final int STATE_LOGIN = 1;
    /**
     * Precrypted stage.
     */
    public static final int STATE_PRECRYPTED = 2;
    /**
     * Crypted stage.
     */
    public static final int STATE_CRYPTED = 3;
    /**
     * Version check stage.
     */
    public static final int CHECK_UPDATE_VERSION = -1;
    /**
     * Update stage.
     */
    public static final int STATE_UPDATE = -2;
    /**
     * World list state.
     */
    public static final int STATE_WORLD_LIST = -3;
    /**
     * Game opcode.
     */
    public static final int OPCODE_GAME = 14;
    /**
     * Update opcode.
     */
    public static final int OPCODE_UPDATE = 15;
    /**
     * World list opcode.
     */
    public static final int OPCODE_WORLD_LIST = 131;
    /**
     * Secure random number generator.
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        final int state = (Integer) session.getAttribute("state", STATE_OPCODE);
        switch (state) {

            case STATE_OPCODE:
                if (in.remaining() >= 1) {                /*
                     * Here we read the first opcode which indicates the type of
                     * connection.
                     * 
                     * 14 = game 15 = update 131: world list
                     * 
                     * Updating is done by sending keys in many 525 servers
                     */
                    final int opcode = in.get() & 0xFF;
                    switch (opcode) {
                        case OPCODE_WORLD_LIST:
                            session.setAttribute("state", STATE_WORLD_LIST);
                            break;
                        case OPCODE_GAME:
                            session.setAttribute("state", STATE_LOGIN);
                            break;
                        case OPCODE_UPDATE:
                            session.setAttribute("state", CHECK_UPDATE_VERSION);
                            break;
                    }
                    return true;
                }
                in.rewind();
                break;

            /*
             * Checks the client version for updating
             */
            case CHECK_UPDATE_VERSION:
                if (in.remaining() >= 4) {
                    final int version = in.getInt();
                    if (version != 508) {
                        logger.info("Incorrect version : " + version);
                        session.write(new PacketBuilder().put((byte) 6).toPacket());
                        session.close(false);
                        in.rewind();
                        return false;
                    }
                    session.write(new PacketBuilder().put((byte) 0).toPacket());
                    session.setAttribute("state", STATE_UPDATE);
                    return true;
                }
                in.rewind();
                break;
            /*
             * WorldList writing
             */
            case STATE_WORLD_LIST:
                if (in.remaining() >= 4) {
                    final int loginOpcode = in.getInt();
                    if (loginOpcode == 0) {
                        session.write(RS2WorldListEncoder.encode(true, true));
                    } else {
                        session.write(RS2WorldListEncoder.encode(false, true));
                    }
                    return true;
                }
                in.rewind();
                break;
            /*
             * Sends the update keys (as i dont have an cache updater implemented
             */
            case STATE_UPDATE:
                if (4 <= in.remaining()) {
                    in.skip(4);
                    final PacketBuilder ukeys = new PacketBuilder();
                    for (final int key : Constants.UPDATE_KEYS) {
                        ukeys.put((byte) key);
                    }
                    session.write(ukeys.toPacket());
                    return true;
                }
                in.rewind();
                break;

            case STATE_LOGIN:
                if (in.remaining() >= 1) {
                    /*
                     * The name hash is a simple hash of the name which is suspected
                     * to be used to select the appropriate login server.
                     */
                    @SuppressWarnings("unused")
                    final int nameHash = in.get() & 0xFF;

                    /*
                     * We generated the server session key using a SecureRandom
                     * class for security.
                     */
                    final long serverKey = RANDOM.nextLong();

                    /*
                     * The initial response is just 0s which the client is set to
                     * ignore (probably some sort of modification).
                     */
                    session.write(new PacketBuilder().put((byte) 0).putLong(serverKey).toPacket());
                    session.setAttribute("state", STATE_PRECRYPTED);
                    session.setAttribute("serverKey", serverKey);
                    return true;
                }
                in.rewind();
                break;
            /*
             * Checks if the login opcodes are correct
             */
            case STATE_PRECRYPTED:
                if (3 <= in.remaining()) {
                    /*
                     * We read the type of login.
                     * 
                     * 16 = normal 18 = reconnection
                     */
                    final int loginOpcode = in.get() & 0xff;
                    if (loginOpcode != 16 && loginOpcode != 18) {
                        logger.info("Invalid login opcode : " + loginOpcode);
                        session.close(false);
                        in.rewind();
                        return false;
                    }
                    /*
                     * We read the size of the login packet.
                     */
                    final int loginSize = in.getUnsignedShort();
                    session.setAttribute("state", STATE_CRYPTED);
                    session.setAttribute("size", loginSize);
                    return true;
                }
                in.rewind();
                break;

            /*
             * Main login information here
             */
            case STATE_CRYPTED:
                final int size = (Integer) session.getAttribute("size");
                if (in.remaining() >= size) {

                    /*
                     * Reads the client version as an <code>in.getInt()</code>
                     */
                    final int version = in.getInt();
                    if (version != RS2Server.VERSION) {
                        logger.info("Incorrect version : " + version);
                        session.close(false);
                        in.rewind();
                        return false;
                    }

                    /*
                     * checks if the client is on low memory Most likely to tell the
                     * server not to play sounds and such
                     */
                    @SuppressWarnings("unused")
                    final boolean lowMemory = (in.get() & 0xFF) == 1 ? true : false;
                    /*
                     * checks if the client is HD
                     */
                    final boolean isHD = false;//(in.get() & 0xff) == 1 ? true : false;

                    in.getInt();
                    for (int i = 0; i < 24; i++) {
                        in.get();
                    }
                    IoBufferUtils.getRS2String(in);

                    for (int i = 0; i < 29; i++) {
                        in.getInt();
                    }
                    in.get();

                    /*
                     * We now read the encrypted block opcode (although in most 317
                     * clients and this server the RSA is disabled) and check it is
                     * equal to 10.
                     */
                    final int blockOpcode = in.get() & 0xFF;
                    if (blockOpcode != 10) {
                        logger.info("Invalid login block opcode : " + blockOpcode);
                        session.close(false);
                        in.rewind();
                        return false;
                    }

                    /*
                     * We read the client's session key.
                     */
                    final long clientKey = in.getLong();

                    /*
                     * And verify it has the correct server session key.
                     */
                    final long serverKey = (Long) session.getAttribute("serverKey");
                    final long reportedServerKey = in.getLong();
                    if (reportedServerKey != serverKey) {
                        logger.info("Server key mismatch (expected : " + serverKey
                                + ", reported : " + reportedServerKey + ")");
                        session.close(false);
                        in.rewind();
                        return false;
                    }

                    final String name = NameUtils.longToName(in.getLong());
                    final String pass = IoBufferUtils.getRS2String(in);
                    logger.info("Login request : username=" + name + " password=" + pass);

                    /*
                     * And setup the ISAAC cipher which is used to encrypt and
                     * decrypt opcodes.
                     */
                    final int[] sessionKey = new int[4];
                    sessionKey[0] = (int) (clientKey >> 32);
                    sessionKey[1] = (int) clientKey;
                    sessionKey[2] = (int) (serverKey >> 32);
                    sessionKey[3] = (int) serverKey;

                    session.removeAttribute("state");
                    session.removeAttribute("serverKey");
                    session.removeAttribute("size");
                    session.removeAttribute("encryptSize");

                    /*
                     * Now, the login has completed, and we do the appropriate
                     * things to fire off the chain of events which will load and
                     * check the saved games etc.
                     */
                    session.getFilterChain().remove("protocol");
                    session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(RS2CodecFactory.GAME));

                    final PlayerDetails pd = new PlayerDetails(session, NameUtils.formatName(name), pass, isHD);
                    World.getWorld().load(pd);
                }
                break;
        }
        in.rewind();
        return false;
    }
}
