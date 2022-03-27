# 连接ftp

## 1.依赖

```xml
<dependency>
    <groupId>commons-net</groupId>
    <artifactId>commons-net</artifactId>
    <version>3.6</version>
</dependency>
```

## 2.工具类

> 遇到问题项目通过容器部署，ftp文件服务器也通过docker部署，通过宿主ip进行访问获取不到ftp服务器的文件，而通过`java -jar xxx.jar`部署都可以访问，这个问题需要设置ftp服务器

```java
ftpClient.enterLocalPassiveMode();
ftpClient.setRemoteVerificationEnabled(false);
```



```java
package com.demo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FTPUtil {

    private String host;
    private String path;
    private int port;
    private String username;
    private String password;

    private FTPClient ftpClient;

    public FTPUtil() {
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public FTPUtil(String host, String path, int port, String username, String password) {
        this.host = host;
        this.path = path;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public FTPClient open() {

        ftpClient = new FTPClient();

        try {

            //每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据。
            //因为ftp server可能每次开启不同的端口来传输数据，但是在linux上或者其他服务器上面，   
            //由于安全限制，可能某些端口没有开启，所以就出现阻塞。
            ftpClient.enterLocalPassiveMode();
            ftpClient.setRemoteVerificationEnabled(false);
            ftpClient.connect(host, port);
            ftpClient.setControlEncoding("UTF8");

            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                throw new RuntimeException("FTP连接失败");
            }

            boolean success = ftpClient.login(username, password);

            if (!success) {
                throw new RuntimeException("FTP登录失败");
            }

            success = ftpClient.changeWorkingDirectory(path);

            if (success) {
                return ftpClient;
            } else {
                throw new RuntimeException("FTP找不到目录");
            }

        } catch (IOException ex) {
            throw new RuntimeException("FTP连接失败");
        }
    }

    public void close() {
        try {
            ftpClient.disconnect();
            ftpClient.logout();
        } catch (IOException e) {
        }
    }

    public InputStream readInputStream(String dir, String fileName) {
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String filePath = dir + File.separator + fileName;
            filePath = filePath.replaceAll("//", "/").replaceAll("\\\\", "/");
            return ftpClient.retrieveFileStream(new String(filePath.getBytes("UTF-8"), "iso-8859-1"));
        } catch (IOException e) {
            throw new RuntimeException("ftp文件读取失败");
        } finally {
            close();
        }
    }

    public byte[] download(String dir, String fileName) {
        try {
            return IOUtils.toByteArray(readInputStream(dir, fileName));
        } catch (IOException e) {
            throw new RuntimeException("ftp文件读取失败");
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        String host = "192.168.1.10";
        int port = 21;
        String username = "test";
        String password = "test123";
        String path = "/test";
        FTPUtil util = new FTPUtil(host, path, port, username, password);

        try {
            FTPClient client = util.open();
            FTPFile[] ftpFiles = client.listFiles();
            for (FTPFile ftpFile : ftpFiles) {
                System.out.println(ftpFile.isFile());
                System.out.println(ftpFile.getName());
            }
            byte[] download = util.download(path, "aaa.zip");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            util.close();
        }
    }
}
```

