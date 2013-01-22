package org.hyperion.rs2.tickable.impl;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.tickable.Tickable;

public class DeathTick extends Tickable {

    private final Entity entity;

    /**
     * Creates the death event for the specified entity.
     *
     * @param entity The player or npc whose death has just happened.
     */
    public DeathTick(final Entity entity) {
        super(15);
        this.entity = entity;
        World.getWorld().submit(new Event(3) {
            @Override
            public void execute() {
                entity.playAnimation(Animation.create(7197));
                stop();
            }
        });
    }

    @Override
    public void execute() {
        if (entity instanceof Player) {
            final Player p = (Player) entity;
            p.getActionSender().sendMessage("Oh dear, you are dead!");
            p.getSkills().restoreLevel(Skills.HITPOINTS);
            entity.setDead(false);
            stop();
        }
    }
}
