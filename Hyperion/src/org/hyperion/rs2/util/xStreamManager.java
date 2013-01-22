package org.hyperion.rs2.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.hyperion.rs2.model.NPCSpawn;

import java.io.InputStream;
import java.io.OutputStream;

public class xStreamManager {

    private static XStream xstream = new XStream(new Sun14ReflectionProvider(), new XppDriver());

    static {
        xstream.alias("npc", NPCSpawn.class);
    }

    public static void save(Object object, OutputStream out) {
        xstream.toXML(object, out);
    }

    public static Object load(InputStream in) {
        return xstream.fromXML(in);
    }
}