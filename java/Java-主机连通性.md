# java测试主机连通性

```java
package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class Test {

    private Logger log = LoggerFactory.getLogger(Test.class);

    /**
     * socket测试ip:port连通性
     * 如果是域名则无法使用此方法
     *
     * @param host
     * @param port
     * @param timeout
     * @return
     */
    private boolean ping(String host, Integer port, Integer timeout) {
        Socket socket = new Socket();
        try {
            String[] split = host.split("\\.");
            byte bytes[] = new byte[4];
            for (int i = 0; i < 4; i++) {
                bytes[i] = (byte) Integer.parseInt(split[i]);
            }
            socket.connect(new InetSocketAddress(InetAddress.getByAddress(bytes), port), timeout);
            return true;
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return false;
    }

    /**
     *  ping测试连通性
     * @param host
     * @param timeout
     * @return
     */
    private boolean ping(String host, Integer timeout) {
        boolean status = false;
        try {
            status = InetAddress.getByName(host).isReachable(timeout);
        } catch (IOException e) {
            log.error("", e);
        }
        return status;
    }

    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://baidu.com");
        String host = url.getHost();
        Integer port = url.getPort();
        Integer timeout = 20000;
        System.out.println(new Test().ping(host, timeout));
    }
}

```
