package org.hyperion.rs2.event;

import java.util.Map;

/**
 * @date 1/20/13
 * @time 8:27 PM
 */
public class Event {

    private String eventName;
    private Map<String, Object> args;

    public Event(String eventName, Map<String, Object> args) {
        this.eventName = eventName;
        this.args = args;
    }

    public String getEventName() {
        return eventName;
    }

    public Map<String, Object> getArgs() {
        return args;
    }
}
