package org.hyperion.rs2.content;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

/**
 * Level up class.
 *
 * @author Linux
 */
public class Levelup {

    /**
     * The skill icon id.
     */
    public static final int[] SKILL_ICON = {100000000, 400000000, 200000000,
            450000000, 250000000, 500000000, 300000000, 1100000000, 1250000000,
            1300000000, 1050000000, 1200000000, 800000000, 1000000000,
            900000000, 650000000, 600000000, 700000000, 1400000000, 1450000000,
            850000000, 1500000000, 1600000000, 1650000000, 0,};

    /**
     * The skill flash id.
     */
    public static final int[] SKILL_FLASH = {1, 4, 2, 64, 8, 16, 32, 32768,
            131072, 2048, 16384, 65536, 1024, 8192, 4096, 256, 128, 512,
            524288, 1048576, 262144, 2097152, 4194304, 8388608, 0,};

    /**
     * Called when a player levels up.
     *
     * @param player The player.
     * @param skill  The skill id.
     */
    public static void level(Player player, int skill) {
        // player.playGraphics(Graphic.create(199, 100));
        player.getActionSender()
                .sendMessage(
                        "You've just advanced a "
                                + Skills.SKILL_NAME[skill]
                                + " level! You have reached level "
                                + player.getSkills().getLevelForExperience(
                                skill) + ".");
        player.getActionSender().sendInterfaceString(
                "Congratulations, you have just advanced a "
                        + Skills.SKILL_NAME[skill] + " level!", 740, 0);
        player.getActionSender()
                .sendInterfaceString(
                        "You have now reached level "
                                + player.getSkills().getLevelForExperience(
                                skill) + ".", 740, 1);
        player.getActionSender().sendConfig(1179, SKILL_ICON[skill]);
        player.getActionSender().sendChatboxInterface(740);
    }

}
