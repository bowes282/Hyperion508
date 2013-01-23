package org.hyperion.rs2.packet.impl;

import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.packet.PacketListener;
import org.hyperion.script.util.Called;

/**
 * Don't really know a better way of doing this but this works.. This class
 * handles all the independent NPC packet options into it's own class
 *
 * @author black flag
 */
public class NpcOptionPacket {

    /**
     * NPC Option Attack
     */
    @Called("npc_attack")
    public static class NpcOptionAttack implements PacketListener {

        public NPC npc;

        public NpcOptionAttack(NPC npc) {
            this.npc = npc;
        }
    }

    @Called("npc_option_1")
    public static class NpcOptionOne implements PacketListener {

        public NPC npc;

        public NpcOptionOne(NPC npc) {
            this.npc = npc;
        }
    }

    @Called("npc_option_2")
    public static class NpcOptionTwo implements PacketListener {

        public NPC npc;

        public NpcOptionTwo(NPC npc) {
            this.npc = npc;
        }
    }

    @Called("npc_option_4")
    public static class NpcOptionThree implements PacketListener {

        public NPC npc;

        public NpcOptionThree(NPC npc) {
            this.npc = npc;
        }
    }
}