package com.ctf.expr.entity;


import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import java.io.Serializable;

/* Car2023 EasyExpr */
public class User implements Serializable {
    private String username;

    private String password;

    private String expr;

    public User() {}

    public User(String username, String password, String expr) {
        this.username = username;
        this.password = password;
        this.expr = expr;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExpr() {
        return this.expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public Boolean filter() {
        String[] BlackList = {
                "\"", "'", "\\u", "invoke", "getClass", "\\x", "$", "{", "}", "@",
                "js", "getRuntime", "java", "script", "ProcessBuilder", "start", "flag" };
//        String[] BlackList = {};
        String str = this.expr.toLowerCase();
        for (String keyword : BlackList) {
            if (str.contains(keyword))
                return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public String getResult() {
        System.out.println("getResult");
        try {
            if (!filter().booleanValue()) {
                OgnlContext ognlContext = new OgnlContext();
                return Ognl.getValue(this.expr, ognlContext).toString();
            }
            else {
                System.out.println("NONONO");
            }
        } catch (OgnlException var2) {
            return "fail";
        }
        return "fail";
    }
}