# Java 文件读取(NIO)

```java
package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioReadFile {

    /**
     *  直接内存（系统内存）
     * @param args
     *
     */
    public static void main(String[] args) {
        try (FileChannel from = new FileInputStream("F:\\dev_tools\\apache-jmeter-5.3.zip").getChannel();
        FileChannel to = new FileOutputStream("F:\\dev_tools\\apache-jmeter-5.3(1).zip").getChannel()){
            System.in.read();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024);
            while (true) {
                int len = from.read(byteBuffer);
                if (len == -1) {
                    break;
                }
                byteBuffer.flip();
                to.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

