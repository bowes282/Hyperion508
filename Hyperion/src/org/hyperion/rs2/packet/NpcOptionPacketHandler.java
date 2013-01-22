package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;

import java.util.logging.Logger;
import org.hyperion.rs2.packet.impl.ButtonClickPacket;
import org.hyperion.rs2.packet.impl.NpcOptionPacket;
import org.hyperion.rs2.packet.impl.NpcOptionPacket.NpcOptionAttack;

/**
 * Created with IntelliJ IDEA. User: black flag Date: 1/15/13 Time: 9:19 PM
 */
public class NpcOptionPacketHandler implements PacketHandler {

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(NpcOptionPacketHandler.class.getName());

    @Override
    public PacketListener handle(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            /**
             * Option 1
             */
            case 7:
                option1(player, packet);
                break;
            /*
             * Option 2.
             */
            case 52:
                option2(player, packet);
                break;
            /*
             * Option 3.
             */
            case 199:
                option3(player, packet);
                break;
            /*
             * Attack.
             */
            case 123:
                return attack(player, packet);
        }
        return new DefaultPacket();
    }

    /**
     * The attack option
     *
     * @param player The player
     * @param packet The packet
     */
    private PacketListener attack(final Player player, Packet packet) {
        int id = packet.getShort() & 0xFFFF;
        
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "NPC Attack Option",
                new Object[]{"id=" + id});

        final NPC npc = (NPC) World.getWorld().getNPCs().get(id);
        player.face(npc.getLocation());
        
        return new NpcOptionAttack(npc);
    }

    /**
     * The first option
     *
     * @param player The player
     * @param packet The packet
     */
    private void option1(final Player player, Packet packet) {
        int id = packet.getShortA() & 0xFFFF;
        if (id < 0 || id >= Constants.MAX_NPCS) {
            return;
        }
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "NPC Option 1", new Object[]{"id=" + id});

        final NPC npc = (NPC) World.getWorld().getNPCs().get(id);
        player.face(npc.getLocation());
    }

    /**
     * The second option
     *
     * @param player The player
     * @param packet The packet
     */
    private void option2(final Player player, Packet packet) {
        int id = packet.getShortA() & 0xFFFF;
        if (id < 0 || id >= Constants.MAX_NPCS) {
            return;
        }
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "NPC Option 2", new Object[]{"id=" + id});

        final NPC npc = (NPC) World.getWorld().getNPCs().get(id);
        player.face(npc.getLocation());
    }

    /**
     * The third option
     *
     * @param player The player
     * @param packet The packet
     */
    private void option3(final Player player, Packet packet) {
        int id = packet.getLEShort() & 0xFFFF;
        if (id < 0 || id >= Constants.MAX_NPCS) {
            return;
        }
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "NPC Option 3", new Object[]{"id=" + id});

        final NPC npc = (NPC) World.getWorld().getNPCs().get(id);
        player.face(npc.getLocation());

    }
}
