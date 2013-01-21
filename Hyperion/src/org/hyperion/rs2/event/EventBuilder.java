package org.hyperion.rs2.event;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class EventBuilder {
    private final String topic;
    private final Map<String, Object> options;

    public EventBuilder(String topic) {
        this(topic, new Hashtable<String, Object>());
    }

    public EventBuilder(String topic, Map<String, Object> options) {
        this.topic = topic;
        this.options = options;
    }

    public EventBuilder withOption(String key, Object value) {
        options.put(key, value);
        return this;
    }

    public EventBuilder withOptions(Map<String, Object> options) {
        return this;
    }

    public Event build() {
        return new Event(topic, options);
    }

    public Map<String, Object> getOptions() {
        return options;
    }
}