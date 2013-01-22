package org.hyperion.script.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.hyperion.script.ScriptContext;
import org.hyperion.script.ScriptEnvironment;
import org.jruby.RubyInstanceConfig;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;

/**
 * @date 1/20/13
 * @time 7:20 PM
 */
public class RubyEnvironment implements ScriptEnvironment {

    /**
     * The scripting container.
     */
    private ScriptingContainer container = new ScriptingContainer();
    /**
     * The script path
     */
    private File scriptBasePath;
    /**
     * The script parameters
     */
    final Map<String, Object> params = new HashMap<>();

    /**
     * Creates the jRuby Script Environment
     *
     * @param scriptStartingPoint The loading path
     */
    public RubyEnvironment(File scriptStartingPoint) {
        this.scriptBasePath = scriptStartingPoint;
        /*
         * The SINGLETHREADED context makes sure that the container and
         * associated scripts are reloaded fully each time the service is
         * refreshed or updated.
         */
        container = new ScriptingContainer(LocalContextScope.SINGLETHREAD);
        container.setLoadPaths(Arrays.asList(scriptStartingPoint.getPath()));
        container.setCompileMode(RubyInstanceConfig.CompileMode.JIT);
    }

    /**
     * Initializes scripting
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void init() throws IOException {
        File f = new File(scriptBasePath + File.separator + "bootstrap.rb");
        try (InputStream is = new FileInputStream(f)) {
            parse(is, f.getAbsolutePath());
        }
    }

    /**
     * Runs a script
     *
     * @param is The script directory
     * @param name The script name
     */
    @Override
    public void parse(InputStream is, String name) {
        container.runScriptlet(is, name);
    }

    /**
     * Sets the script context
     *
     * @param context The context to set
     */
    @Override
    public void setContext(ScriptContext context) {
        container.put("game", context);
    }

    /**
     * Calls the scripts
     *
     * @param eventName The script event name
     * @param params The script parameters
     */
    @Override
    public void callScripts(String eventName, Map<String, Object> params) {
        container.callMethod(null, "execute_event", eventName, params);
    }

    /**
     * Sets the script parameters
     *
     * @param key The key
     * @param args The arguments
     */
    @Override
    public RubyEnvironment setParams(String key, Object args) {
        params.put(key, args);
        return this;
    }

    /**
     * Get the parameters
     *
     * @return params The parameters
     */
    @Override
    public Map<String, Object> getParams() {
        return params;
    }
}
