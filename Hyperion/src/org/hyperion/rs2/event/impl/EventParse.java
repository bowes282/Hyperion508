package org.hyperion.rs2.event.impl;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.Player;

/**
 * @date 1/20/13
 * @time 9:09 PM
 */
public interface EventParse {

    public void execute(Player player, Event event);
}
