package com.ycbjava.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlUploadUtil {
    public static boolean uploadfile(String filename, String content) {
        if (filename != null && !filename.endsWith(".ftl"))
            return false;
        String realPath = "/app/templates/" + filename;
//        String realPath = "templates/" + filename;
        if (realPath.contains("../") || realPath.contains("..\\"))
            return false;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(realPath));
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e.getMessage());
            return false;
        }
    }
}
