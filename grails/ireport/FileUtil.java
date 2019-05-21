package com.common.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *  文件流转换
 */
public enum FileUtil {

    E;

    public ByteArrayOutputStream fileToByteArrayOutPutStream(File file) throws IOException {
        ByteArrayOutputStream out = null;
        FileInputStream inputStream = null;
        try {
            out = new ByteArrayOutputStream();
            inputStream = new FileInputStream(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            out.write(buffer);
        } finally {
            if(inputStream!=null) inputStream.close();
        }
        return out;
    }
}
