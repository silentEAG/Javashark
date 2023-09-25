package com.ycbjava.Utils;


import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class HtmlInvocationHandler implements InvocationHandler, Serializable {
    public Map obj;

    public HtmlInvocationHandler() {}

    public HtmlInvocationHandler(Map obj) {
        this.obj = obj;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = this.obj.get(method.getName());
        return result;
    }
}
