
package com.ycbjava.Bean;

import com.ycbjava.Utils.HtmlMap;
import java.io.Serializable;
import java.util.Map;

public class HtmlBean implements Serializable {
    public Map HtmlMap;
    public String filename;
    public String content;

    public HtmlBean(HtmlMap htmlMap, String filename, String content) {
        this.HtmlMap = htmlMap;
        this.filename = filename;
        this.content = content;
    }

    public HtmlBean() {
    }

    public Map getHtmlMap() {
        this.HtmlMap.put(this.filename, this.content);
        return this.HtmlMap;
    }

    public void setHtmlMap(Map htmlMap) {
        this.HtmlMap = htmlMap;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
