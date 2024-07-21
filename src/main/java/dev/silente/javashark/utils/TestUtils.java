package dev.silente.javashark.utils;

import dev.silente.javashark.sink.jdk.STemplates;

import java.io.Serializable;

public class TestUtils implements Serializable {

    public static void _main(String[] argv) throws Exception {
        Runtime.getRuntime().exec("calc");
    }

    public static void main(String[] args) throws Exception {
        Object o = STemplates.getEvilTemplates("calc");

    }

    public String name;
    String addr;
    private int age;

    static {
        System.out.println("static!");
    }

    public TestUtils() {
        name = "SilentE";
        addr = "none";
        age = 114514;
    }

    public String getName() {
        System.out.println("getName");
        return name;
    }

    public int getAge() {
        System.out.println("getAge");
        return age;
    }

    public void setName(String name) {
        System.out.println("setName");
        this.name = name;
    }

    public void setAge(int age) {
        System.out.println("setAge");
        this.age = age;
    }

    @Override
    public String toString() {
        System.out.println("toString!");
        return "";
    }
}
