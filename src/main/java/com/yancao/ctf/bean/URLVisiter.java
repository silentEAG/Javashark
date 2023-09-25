package com.yancao.ctf.bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;

public class URLVisiter implements Serializable {
    public String visitUrl(String myurl) {
        if (myurl.startsWith("file"))
            return "file protocol is not allowed";
        URL url = null;
        try {
            url = new URL(myurl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine);
            in.close();
            return sb.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }
}
