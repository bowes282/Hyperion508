package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.net.Packet;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA. User: black flag Date: 1/16/13 Time: 5:51 PM
 */
public class OperateItemPacketHandler implements PacketHandler {

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(OperateItemPacketHandler.class.getName());
    /**
     * Operate Item
     */
    private final static int OPERATE_ITEM = 186;

    @Override
    public PacketListener handle(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            /**
             * Operate item
             */
            case OPERATE_ITEM:
                operateItem(player, packet);
                break;
        }
        return new DefaultPacket();
    }

    /**
     * Handles item on operate
     *
     * @param player The player
     * @param packet The packet
     */
    private void operateItem(Player player, Packet packet) {
        int interfaceSet = packet.getInt();
        int interfaceId = interfaceSet >> 16;
        int id = packet.getShortA() & 0xFFFF;
        int slot = packet.getLEShortA() & 0xFFFF;
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Operate Item",
                new Object[]{"id=" + id, "slot=" + slot, "interface=" + interfaceId});
        switch (interfaceId) {
            case 7168:
                if (slot < 0 || slot >= Equipment.SIZE || player.getEquipment().get(slot) == null) {
                    return;
                }
                if (player.getEquipment().get(slot).getId() != id) {
                    return;
                }
                //  ScriptManager.getInstance().invoke("item_operate_" + id, player, slot);
                break;
            default:
                logger.info("Unhandled item operate [id=" + id + " slot=" + slot + " interface=" + interfaceId + "]");
                break;
        }
    }
}
