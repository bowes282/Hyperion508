package org.hyperion.rs2.packet;

import org.hyperion.rs2.content.Emotes;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.packet.impl.ButtonClickPacket;

import java.util.logging.Logger;

public class ActionButtonPacketHandler implements PacketHandler {

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ActionButtonPacketHandler.class.getName());

    @Override
    public PacketListener handle(Player player, Packet packet) {
        int interfaceId = packet.getShort() & 0xFFFF;
        int button = packet.getShort() & 0xFFFF;
        int childButton = 0;
        if (packet.getLength() >= 6) {
            childButton = packet.getShort() & 0xFFFF;
        }
        if (childButton == 65535) {
            childButton = 0;
        }

        player.getActionSender().sendDebugPacket(packet.getOpcode(), "ActionButton",
                new Object[]{"interface=" + interfaceId, " button=" + button, " child=" + childButton});

        switch (interfaceId) {

            /*
            * Setting tab.
            */
            case 261:
                switch (button) {
                 /*
                  * Running toggle.
                  */
                    case 3:
                        if (!player.getWalkingQueue().isRunningToggled()) {
                            player.getWalkingQueue().setRunningToggled(true);
                            player.getActionSender().sendConfig(173, 1);
                        } else {
                            player.getWalkingQueue().setRunningToggled(false);
                            player.getActionSender().sendConfig(173, 0);
                        }
                        break;
                }
                break;

            /*
            * The emotion tab.
            */
            case 464:
                /*
                 * Emote tab.
                 */
                if (!Emotes.emote(player, button)) {
                    player.getActionSender().sendMessage("Sorry, that emote isn't supported!");
                }
                break;

            /*
            * Map buttons.
            */
            case 750:
                switch (button) {
                    /*
                  * Running toggle.
                  */
                    case 1:
                        if (!player.getWalkingQueue().isRunningToggled()) {
                            player.getWalkingQueue().setRunningToggled(true);
                            player.getActionSender().sendConfig(173, 1);
                        } else {
                            player.getWalkingQueue().setRunningToggled(false);
                            player.getActionSender().sendConfig(173, 0);
                        }
                        break;
                }
                break;
            case 763:
                /*
                 * Inventory interface with banking.
                 */
                if (button == 0) {
                    switch (packet.getOpcode()) {
                        case 165:
                            Bank.deposit(player, childButton, 1);
                            break;
                        case 2:
                            Bank.deposit(player, childButton, 5);
                            break;
                        case 178:
                            Bank.deposit(player, childButton, 10);
                            break;
                        case 166:
                            /*
                            * Max deposit
                            */
                            break;
                        case 223:
                            /*
                            * Examine.
                            */
                            break;
                    }
                }
                break;
            case 762:
                /*
                 * Bank interface.
                 */
                switch (button) {
                    case 73:
                        switch (packet.getOpcode()) {
                            case 165:
                                Bank.withdraw(player, childButton, 1);
                                break;
                            case 2:
                                Bank.withdraw(player, childButton, 5);
                                break;
                            case 178:
                                Bank.withdraw(player, childButton, 10);
                                break;
                            case 166:
                            /*
                             * Max deposit
                             */
                                break;
                            case 223:
                            /*
                             * Examine.
                             */
                                break;
                        }
                        break;
                }
                break;
            default:
                logger.info("Unhandled action button " + button + " on interface " + interfaceId);
                break;
        }
        return new ButtonClickPacket(interfaceId, button, childButton);
    }
}
