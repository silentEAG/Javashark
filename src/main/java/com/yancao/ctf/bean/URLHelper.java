package com.yancao.ctf.bean;


import com.yancao.ctf.bean.URLVisiter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class URLHelper implements Serializable {
    public String url;

    public URLVisiter visiter = null;

    private static final long serialVersionUID = 1L;

    public URLHelper(String url) {
        this.url = url;
    }

    private void readObject(ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        if (this.visiter != null) {
            String result = this.visiter.visitUrl(this.url);
            File file = new File("./file");
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(result.getBytes());
            fos.close();
        }
    }
}
