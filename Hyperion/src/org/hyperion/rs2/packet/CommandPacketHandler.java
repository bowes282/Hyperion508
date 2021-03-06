package org.hyperion.rs2.packet;

import org.hyperion.rs2.content.Levelup;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.Player.Rights;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.packet.impl.CommandPacket;

public class CommandPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        final String commandString = packet.getRS2String();
        final String[] args = commandString.split(" ");
        final String command = args[0].toLowerCase();
        try {
             if (command.equals("spawn")) {
                NPC npc = new NPC(NPCDefinition.forId(Integer.parseInt(args[1])));
                npc.setLocation(player.getLocation());
                Region region = World.getWorld().getRegionManager().getRegionByLocation(npc.getLocation());
                region.addNpc(npc);
                World.getWorld().register(npc);
            } else if (command.equals("levelup")) {
                Levelup.level(player, Integer.parseInt(args[1]));
            } else if (command.equals("threads")) {
                player.getActionSender().sendMessage("There are currently " + Thread.activeCount() + " active thread(s) running!");
            } else if (command.equals("interface")) {
                player.getActionSender().sendInterface(Integer.valueOf(args[1]));
            } else if (command.equals("anim")) {
                if (args.length == 2 || args.length == 3) {
                    final int id = Integer.parseInt(args[1]);
                    int delay = 0;
                    if (args.length == 3) {
                        delay = Integer.parseInt(args[2]);
                    }
                    player.playAnimation(Animation.create(id, delay));
                    player.getUpdateFlags().flag(UpdateFlag.ANIMATION);
                }
            } else if (command.equals("gfx")) {
                if (args.length == 2 || args.length == 3) {
                    final int id = Integer.parseInt(args[1]);
                    int delay = 0;
                    if (args.length == 3) {
                        delay = Integer.parseInt(args[2]);
                    }
                    player.playGraphics(Graphic.create(id, delay));
                }
            } else if (command.equals("max")) {
                for (int i = 0; i <= Skills.SKILL_COUNT; i++) {
                    player.getSkills().setLevel(i, 99);
                    player.getSkills().setExperience(i, 13034431);
                }
            } else if (command.startsWith("lvl")) {
                try {
                    player.getSkills().setLevel(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                    player.getSkills().setExperience(Integer.parseInt(args[1]), player.getSkills().getXPForLevel(Integer.parseInt(args[2])) + 1);
                    player.getActionSender().sendMessage(Skills.SKILL_NAME[Integer.parseInt(args[1])] + " level is now " + Integer.parseInt(args[2]) + ".");
                } catch (final Exception e) {
                    e.printStackTrace();
                    player.getActionSender().sendMessage("Syntax is ::lvl [skill] [lvl].");
                }
            } else if (command.startsWith("skill")) {
                try {
                    player.getSkills().setLevel(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                    player.getActionSender().sendMessage(Skills.SKILL_NAME[Integer.parseInt(args[1])] + " level is temporarily boosted to " + Integer.parseInt(args[2]) + ".");
                } catch (final Exception e) {
                    e.printStackTrace();
                    player.getActionSender().sendMessage("Syntax is ::skill [skill] [lvl].");
                }
            }
        } catch (final Exception ex) {
            player.getActionSender().sendMessage("Error while processing command.");
        }
        return new CommandPacket(command, args);
    }
}
