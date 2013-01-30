package org.hyperion.rs2.model;

import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.region.Region;

public class NPC extends Entity {

    /**
     * The definition.
     */
    private final NPCDefinition definition;

    /**
     * Creates the NPC with the specified definition.
     *
     * @param definition The definition.
     */
    public NPC(NPCDefinition definition) {
        super();
        this.definition = definition;
    }

    /**
     * Gets the health points of the NPC.
     *
     * @return 10 Random number TODO: to add health
     */
    public int getHealth() {
        return 10;
    }

    /**
     * Gets the NPC definition.
     *
     * @return The NPC definition.
     */
    public NPCDefinition getDefinition() {
        return definition;
    }

    @Override
    public void addToRegion(Region region) {
        region.addNpc(this);
    }

    @Override
    public void removeFromRegion(Region region) {
        region.removeNpc(this);
    }

    @Override
    public int getClientIndex() {
        return getIndex();
    }

    /**
     * Manages update flags and HP modification when a hit occurs.
     *
     * @param source The Entity dealing the blow.
     */
    @Override
    public void inflictDamage(int damage, HitType type) {
        if (!getUpdateFlags().get(UpdateFlags.UpdateFlag.HIT)) {
            Hit hit = new Hit(damage, type);
            getDamage().setHit1(hit);
            getUpdateFlags().flag(UpdateFlags.UpdateFlag.HIT);
        } else {
            if (!getUpdateFlags().get(UpdateFlags.UpdateFlag.HIT_2)) {
                Hit hit = new Hit(damage, type);
                getDamage().setHit2(hit);
                getUpdateFlags().flag(UpdateFlags.UpdateFlag.HIT_2);
            }
        }
        //skills.detractLevel(Skills.HITPOINTS, inc.getDamage());
        setInCombat(true);
        /*
         if (skills.getLevel(Skills.HITPOINTS) <= 0) {
         if (!isDead()) {
         World.getWorld().submit(new DeathTick(this));
         }
         setDead(true);
         }*/
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isNPC() {
        return true;
    }
}