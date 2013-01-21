package org.hyperion.script.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.script.GameContext;
import org.hyperion.script.ScriptEnvironment;
import org.jruby.RubyInstanceConfig;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

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
     * Parses the bootstrapper.
     *
     * @throws IOException if an I/O error occurs.
     */
    private void parseBootstrapper() throws IOException {
        File f = new File(Constants.SCRIPTS_DIR.getPath() + "/bootstrap.rb");
        InputStream is = new FileInputStream(f);
        try {
            parse(is, f.getAbsolutePath());
        } finally {
            is.close();
        }
    }

    @Override
    public void parse(InputStream is, String name) {
        container.runScriptlet(is, name);
    }

    @Override
    public void setContext(GameContext context) {
        container.put("$ctx", context);
    }

    private final String scriptBasePath;

    public RubyEnvironment(String scriptStartingPoint) {
        this.scriptBasePath = scriptStartingPoint;

		/*
         * The SINGLETHREADED context makes sure that the container and
		 * associated scripts are reloaded fully each time the service is
		 * refreshed or updated.
		 */
        container = new ScriptingContainer(LocalContextScope.SINGLETHREAD);
        container.setLoadPaths(Arrays.asList(scriptStartingPoint));
        container.setCompileMode(RubyInstanceConfig.CompileMode.JIT);
    }

    public void callScripts(String eventName, Map<String, Object> params) {
        container.callMethod(null, "fire_event", eventName, params);
    }

    public void start() {
        container.runScriptlet(PathType.ABSOLUTE, scriptBasePath + File.separator + "bootstrap.rb");
    }

    public static void main(String args[]) {
        new RubyEnvironment(Constants.SCRIPTS_DIR.getPath()).start();
    }

    public void stop() {
        container.clear();
    }

}
