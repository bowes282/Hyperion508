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
}