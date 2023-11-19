package com.example.demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Ping implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;

    private String arg1;

    private String arg2;

    public void setCommand(String command) {
        this.command = command;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String[] cmdArray = { this.command, this.arg1, this.arg2 };
        Runtime.getRuntime().exec(cmdArray);
    }
}
