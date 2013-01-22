package org.hyperion.script;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.hyperion.script.impl.RubyEnvironment;

/**
 * Represents some sort of environment that plugins could be executed in, e.g.
 * {@code javax.script} or Jython.
 *
 * @author Graham
 */
public interface ScriptEnvironment {

    /**
     * Loads up the script files.
     */
    public void init() throws IOException;

    /**
     * Parses the input stream.
     *
     * @param is The input stream.
     * @param name The name of the file.
     */
    public void parse(InputStream is, String name);

    /**
     * Sets the context for this environment.
     *
     * @param context The context.
     */
    public void setContext(ScriptContext context);

    /**
     * Calls the scripts
     *
     * @param eventName The script event name
     * @param params The script parameters
     */
    public void callScripts(String eventName, Map<String, Object> params);

    /**
     * Sets script parameters
     *
     * @param key The key (name)
     * @param args The arguments
     * @return RubyEnvironment for chaining
     */
    public RubyEnvironment setParams(String key, Object args);

    /**
     * Gets the parameters
     *
     * @return The parameters
     */
    public Map<String, Object> getParams();
}