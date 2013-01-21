package org.hyperion.rs2.content;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Player;

/**
 * Handles click an emote.
 *
 * @author Graham
 */
public class Emotes {

    /**
     * Handles a player emote: does the appropriate animation.
     *
     * @param player
     * @param buttonId
     * @return
     */
    public static boolean emote(Player player, int buttonId) {
        if (buttonId == 2) {
            player.playAnimation(Animation.create(855));
        } else if (buttonId == 3) {
            player.playAnimation(Animation.create(856));
        } else if (buttonId == 4) {
            player.playAnimation(Animation.create(858));
        } else if (buttonId == 5) {
            player.playAnimation(Animation.create(859));
        } else if (buttonId == 6) {
            player.playAnimation(Animation.create(857));
        } else if (buttonId == 7) {
            player.playAnimation(Animation.create(863));
        } else if (buttonId == 8) {
            player.playAnimation(Animation.create(2113));
        } else if (buttonId == 9) {
            player.playAnimation(Animation.create(862));
        } else if (buttonId == 10) {
            player.playAnimation(Animation.create(864));
        } else if (buttonId == 11) {
            player.playAnimation(Animation.create(861));
        } else if (buttonId == 12) {
            player.playAnimation(Animation.create(2109));
        } else if (buttonId == 13) {
            player.playAnimation(Animation.create(2111));
        } else if (buttonId == 14) {
            player.playAnimation(Animation.create(866));
        } else if (buttonId == 15) {
            player.playAnimation(Animation.create(2106));
        } else if (buttonId == 16) {
            player.playAnimation(Animation.create(2107));
        } else if (buttonId == 17) {
            player.playAnimation(Animation.create(2108));
        } else if (buttonId == 18) {
            player.playAnimation(Animation.create(860));
        } else if (buttonId == 19) {
            player.playAnimation(Animation.create(0x558));
            player.playGraphics(Graphic.create(574));
        } else if (buttonId == 20) {
            player.playAnimation(Animation.create(2105));
        } else if (buttonId == 21) {
            player.playAnimation(Animation.create(2110));
        } else if (buttonId == 22) {
            player.playAnimation(Animation.create(865));
        } else if (buttonId == 23) {
            player.playAnimation(Animation.create(2112));
        } else if (buttonId == 24) {
            player.playAnimation(Animation.create(0x84F));
        } else if (buttonId == 25) {
            player.playAnimation(Animation.create(0x850));
        } else if (buttonId == 26) {
            player.playAnimation(Animation.create(1131));
        } else if (buttonId == 27) {
            player.playAnimation(Animation.create(1130));
        } else if (buttonId == 28) {
            player.playAnimation(Animation.create(1129));
        } else if (buttonId == 29) {
            player.playAnimation(Animation.create(1128));
        } else if (buttonId == 30) {
            player.playAnimation(Animation.create(4275));
        } else if (buttonId == 31) {
            player.playAnimation(Animation.create(1745));
        } else if (buttonId == 32) {
            player.playAnimation(Animation.create(4280));
        } else if (buttonId == 33) {
            player.playAnimation(Animation.create(4276));
        } else if (buttonId == 34) {
            player.playAnimation(Animation.create(3544));
        } else if (buttonId == 35) {
            player.playAnimation(Animation.create(3543));
        } else if (buttonId == 36) {
            player.playAnimation(Animation.create(7272));
            player.playGraphics(Graphic.create(1244));
        } else if (buttonId == 37) {
            player.playAnimation(Animation.create(2836));
        } else if (buttonId == 38) {
            player.playAnimation(Animation.create(6111));
        } else if (buttonId == 39) {
            Skillcapes.emote(player);
        } else if (buttonId == 40) {
            player.playAnimation(Animation.create(7531));
        } else if (buttonId == 41) {
            player.playAnimation(Animation.create(2414));
            player.playGraphics(Graphic.create(1537));
        } else if (buttonId == 42) {
            player.playAnimation(Animation.create(8770));
            player.playGraphics(Graphic.create(1553));
        } else if (buttonId == 43) {
            player.playAnimation(Animation.create(9990));
            player.playGraphics(Graphic.create(1734));
        } else {
            return false;
        }
        return true;
    }
}
