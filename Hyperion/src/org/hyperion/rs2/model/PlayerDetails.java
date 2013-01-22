package org.hyperion.rs2.model;

import org.apache.mina.core.session.IoSession;
import org.hyperion.script.util.Called;

/**
 * @author 'Mystic Flow
 */
@Called("login")
public final class PlayerDetails {

    /**
     * The session.
     */
    private final IoSession session;
    /**
     * The player name.
     */
    private final String name;
    /**
     * The player password.
     */
    private final String pass;
    /**
     * The player using hd.
     */
    private final boolean isHD;

    /**
     * Creates the player details class.
     *
     * @param session The session.
     * @param name The name.
     * @param pass The password.
     * @param isHD The HD state
     */
    public PlayerDetails(IoSession session, String name, String pass, boolean isHD) {
        this.session = session;
        this.name = name;
        this.pass = pass;
        this.isHD = isHD;
    }

    /**
     * Gets the
     * <code>IoSession</code>.
     *
     * @return The <code>IoSession</code>.
     */
    public IoSession getSession() {
        return session;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the password.
     *
     * @return The password.
     */
    public String getPassword() {
        return pass;
    }

    /**
     * Gets the HD client.
     *
     * @return The HD.
     */
    public boolean isHD() {
        return isHD;
    }
}
