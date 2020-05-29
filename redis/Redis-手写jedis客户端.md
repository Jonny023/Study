```java
package com.example.redisson.jedis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Jonny
 * @description redis客户端实现
 * @date 2020/5/29
 */
public class JedisClient {
    private Socket socket;
    private OutputStream writer;
    private InputStream reader;

    public JedisClient(String host, int port) throws IOException {
        //与Redis服务端的Socket连接
        socket = new Socket(host,port);
        writer = socket.getOutputStream();
        reader = socket.getInputStream();

    }
    //密码验证 *2$5\r\nauth\r\n$5\r\n12345
    public String auth(String password) throws IOException {
        StringBuffer conmmand = new StringBuffer();
        //*2：表示有两个字符串
        conmmand.append("*2").append("\r\n");
        //$4：表示auth长度为4
        conmmand.append("$4").append("\r\n");
        conmmand.append("auth").append("\r\n");
        conmmand.append("$").append(password.getBytes().length).append("\r\n");
        conmmand.append(password).append("\r\n");
        return exceConmmand(conmmand);
    }
    //set命令
    public String set(String key,String value) throws IOException {
        StringBuffer conmmand = new StringBuffer();
        conmmand.append("*3").append("\r\n");
        conmmand.append("$3").append("\r\n");
        conmmand.append("SET").append("\r\n");
        conmmand.append("$").append(key.getBytes().length).append("\r\n");
        conmmand.append(key).append("\r\n");
        conmmand.append("$").append(value.getBytes().length).append("\r\n");
        conmmand.append(value).append("\r\n");
        return  exceConmmand(conmmand);
    }
    //get key
    public String get(String key) throws IOException{
        StringBuffer conmmand = new StringBuffer();
        conmmand.append("*2").append("\r\n");
        conmmand.append("$3").append("\r\n");
        conmmand.append("get").append("\r\n");
        conmmand.append("$").append(key.getBytes().length).append("\r\n");
        conmmand.append(key).append("\r\n");
        return exceConmmand(conmmand);
    }
    //写命令
    public String exceConmmand(StringBuffer conmmand) throws IOException {
        writer.write(conmmand.toString().getBytes());
        byte[] result = new byte[1024];
        reader.read(result);
        return new String(result);
    }
}
```
