# jsch工具连接sftp

## 1.pom依赖

```xml
<dependency>
    <groupId>com.jcraft</groupId>
    <artifactId>jsch</artifactId>
    <version>0.1.54</version>
</dependency>
```

## 2.自定义类

```java
package utils;

public class FileInfo {

    /**
     *  文件名
     */
    private String fileName;

    /**
     *  文件路径
     */
    private String path;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
```

## 3.工具类

```java
package utils;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SFTPUtil {

    private transient Logger log = LoggerFactory.getLogger(SFTPUtil.class);

    public SFTPUtil() {
    }

    private ChannelSftp sftp;

    private Session session;

    /**
     * SFTP 登录用户名
     */
    private String username;

    /**
     * SFTP 登录密码
     */
    private String password;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * SFTP 服务器地址IP地址
     */
    private String host;

    /**
     * SFTP 端口
     */
    private int port;


    /**
     * 构造基于密码认证的sftp对象
     */
    public SFTPUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     */
    public SFTPUtil(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    /**
     * 连接sftp服务器
     */
    public void login() {
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                jsch.addIdentity(privateKey);// 设置私钥
            }

            session = jsch.getSession(username, host, port);

            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            //跳过 Kerberos username 身份验证提示
            config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            log.error("{}", e);
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }


    /**
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
     *
     * @param basePath     服务器的基础路径
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     */
    public void upload(String basePath, String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(basePath);
            sftp.cd(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String[] dirs = directory.split("/");
            String tempPath = basePath;
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) continue;
                tempPath += "/" + dir;
                try {
                    sftp.cd(tempPath);
                } catch (SftpException ex) {
                    sftp.mkdir(tempPath);
                    sftp.cd(tempPath);
                }
            }
        }
        sftp.put(input, sftpFileName);  //上传文件
    }


    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException, NoSuchFieldException, IllegalAccessException {
        if (StringUtils.isNoneBlank(directory)) {
            sftp.cd(directory);
        }

        Class<?> cl = ChannelSftp.class;
        Field f = cl.getDeclaredField("server_version");
        f.setAccessible(true);
        f.set(sftp, 2);
        sftp.setFilenameEncoding("UTF-8");

        InputStream is = sftp.get(downloadFile);

        byte[] fileData = IOUtils.toByteArray(is);

        return fileData;
    }


    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) throws SftpException {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /**
     * 切换目录
     *
     * @param remotePath
     * @param filenamePattern
     * @param channelSftp
     * @return
     */
    public List<ChannelSftp.LsEntry> lsFiles(String remotePath, Pattern filenamePattern, ChannelSftp channelSftp) {
        List<ChannelSftp.LsEntry> lsEntryList = null;
        try {
            Vector<ChannelSftp.LsEntry> vector = channelSftp.ls(remotePath);
            if (vector != null) {
                lsEntryList = vector.stream().filter(x -> {
                    boolean match = true;
                    if (filenamePattern != null) {
                        Matcher mtc = filenamePattern.matcher(x.getFilename());
                        match = mtc.find();
                    }
                    if (match && !x.getAttrs().isDir() && !x.getAttrs().isLink()) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
            }
        } catch (SftpException e) {
            lsEntryList = null;
            e.printStackTrace();
        }
        return lsEntryList;
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     */
    public List<ChannelSftp.LsEntry> listFiles(String directory) throws SftpException {
        Vector<ChannelSftp.LsEntry> vector = sftp.ls(directory);
        List<ChannelSftp.LsEntry> lsEntryList = null;
        if (vector != null) {
            lsEntryList = vector.stream().filter(x -> {
                if (!x.getAttrs().isDir() && !x.getAttrs().isLink()) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
        }
        return lsEntryList;
    }

    /**
     * 通过正则读取指定文件
     *
     * @param dir
     * @param regex
     * @return
     */
    public List<FileInfo> listFiles(String dir, String regex) {
        Pattern filenamePattern = Pattern.compile(regex);
        Class<?> cl = ChannelSftp.class;
        Field f;
        FileInfo fileInfo;
        List<FileInfo> list = new ArrayList<>();
        try {
            f = cl.getDeclaredField("server_version");
            f.setAccessible(true);
            f.set(sftp, 2);
            sftp.setFilenameEncoding("UTF-8");
            List<ChannelSftp.LsEntry> dirs = lsFiles(dir, filenamePattern, sftp);
            StringBuilder sb;
            for (ChannelSftp.LsEntry d : dirs) {
                fileInfo = new FileInfo();
                sb = new StringBuilder();
                sb.append(dir);
                sb.append("/");
                sb.append(d.getFilename());
                fileInfo.setFileName(d.getFilename());
                fileInfo.setPath(sb.toString().replaceAll("//", "/"));
                list.add(fileInfo);
            }
            sftp.disconnect();
        } catch (NoSuchFieldException | IllegalAccessException | SftpException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws SftpException, IOException {
        SFTPUtil sftp = new SFTPUtil("mysftp", "123456", "47.96.96.254", 22);
        sftp.login();


        //上传文件
//        File file = new File("D:\\img\\a.png");
//        InputStream is = new FileInputStream(file);
//        sftp.upload("/", "/img", "a.png", is);

        List<ChannelSftp.LsEntry> fileLists = sftp.listFiles("/");
        fileLists.forEach(f -> {
            System.out.println(f.getFilename());
        });


        //过滤文件
        String regex = "^(?:[\\u4e00-\\u9fa5_a-zA-Z0-9]+\\.xlsx|[\\u4e00-\\u9fa5_a-zA-Z0-9]+\\.xls|[\\u4e00-\\u9fa5_a-zA-Z0-9]+\\.csv)$";
        List<FileInfo> fileInfos = sftp.listFiles("/", regex);
        System.out.println(fileInfos);

        sftp.logout();
    }
}
```

