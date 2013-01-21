package org.hyperion.rs2.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 1/20/13
 * @time 8:26 PM
 */
public class GameEventManager {

    /**
     * A map of event listeners.
     */
    public final Map<String, Event> listeners = new HashMap<String, Event>();

    public GameEventManager() {
        // not in a plugin so it is harder for people to remove!
        // listeners.put("button_click", new Event("buttonEvent"));
        System.out.println("Loaded " + listeners.size() + " listeners");
    }


    public void parse(String name, Event event) {
        listeners.put(name.toLowerCase(), event);
    }
}
