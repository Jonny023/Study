# ffprobe判断rtmp是否可用

[runtime教程](https://www.infoworld.com/article/2071275/when-runtime-exec---won-t.html?page=2)

## 实现callable接口

```java
package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.Callable;

public class CommandThread implements Callable<String> {

    private final Logger LOG = LoggerFactory.getLogger(CommandThread.class);

    InputStream is;
    String type;
    OutputStream os;

    CommandThread(InputStream is, String type) {
        this(is, type, null);
    }

    CommandThread(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    @Override
    public String call() {
        try {
            StringBuilder sb = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
```

## 创建测试类

```java
package com.test;

import java.io.FileOutputStream;
import java.util.concurrent.FutureTask;

/**
 *  判断rtmp是否可用
 */
public class RtmpValidTest {

    public static void main(String[] args) {
        try {
            FileOutputStream fos = new FileOutputStream("C:\\tmp\\file.log");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("ffprobe -v quiet -print_format json -show_streams rtmp://ns8.indexforce.com/home/mystream");
            CommandThread cmdError = new CommandThread(proc.getErrorStream(), "ERROR");
            CommandThread cmdOutput = new CommandThread(proc.getInputStream(), "OUTPUT", fos);
            FutureTask<String> result1 = new FutureTask<>(cmdError);
            FutureTask<String> result2 = new FutureTask<>(cmdOutput);

            new Thread(result1).start();
            new Thread(result2).start();

            System.out.println("error: " + result1.get());
            System.out.println("info: " + result2.get());

            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
            fos.flush();
            fos.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
```

```java
String cmd = "ffprobe -v quiet -print_format json -show_streams " + streamAddress;
try {
    Runtime rt = Runtime.getRuntime();
    Process proc = rt.exec(cmd);
    return proc.waitFor() == 0;
} catch (Throwable t) {
    log.error("异常");
}

```

