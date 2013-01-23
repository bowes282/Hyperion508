package org.hyperion.rs2.packet.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.packet.PacketListener;
import org.hyperion.script.util.Called;

/**
 * Don't really know a better way of doing this but this works.. This class
 * handles all the independent Player packet options into it's own class
 *
 * @author black flag
 */
public class PlayerOptionPacket {

    @Called("player_attack")
    public static class PlayerOptionAttack implements PacketListener {

        public Player target;

        public PlayerOptionAttack(Player target) {
            this.target = target;
        }
    }

    @Called("player_option_2")
    public static class PlayerOptionTwo implements PacketListener {

        public Player target;

        public PlayerOptionTwo(Player target) {
            this.target = target;
        }
    }

    @Called("player_option_3")
    public static class PlayerOptionThree implements PacketListener {

        public Player target;

        public PlayerOptionThree(Player target) {
            this.target = target;
        }
    }
}
