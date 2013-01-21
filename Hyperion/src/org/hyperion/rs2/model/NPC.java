package org.hyperion.rs2.model;

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

    @Override
    public void inflictDamage(int damage, HitType type) {
        // TODO Auto-generated method stub
    }
}