* 文件工具类

```java
package com.gd.common.util;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.MessageFormat;

/**
 * 说明：文件处理
 */
public class FileUtil {


    /**
     * 获取文件大小 返回 KB 保留2位小数  没有文件时返回0
     *
     * @param filepath 文件完整路径，包括文件名
     * @return
     */
    public static String getFilesize(String filepath) {
        File backupath = new File(filepath);
        return String.valueOf(backupath.length() / 1000.00);
    }

    /**
     * 获取文件大小 返回 B,KB,MB,GB
     *
     * @param filePath 文件完整路径，包括文件名
     * @return
     */
    @SneakyThrows
    public static String getPrintSize(String filePath) {
        FileInputStream file = null;
        long size = 0;
        try {
            file = new FileInputStream(filePath);
            FileChannel fileChannel = file.getChannel();
            size = fileChannel.size();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.close();
        }

        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        double value = (double) size;
        return booleanConveartString(value);

    }

    public static String booleanConveartString(double value) {
        if (value < 1024) {
            return String.valueOf(value) + "B";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (value < 1024) {
            return String.valueOf(value) + "KB";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        if (value < 1024) {
            return String.valueOf(value) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            return String.valueOf(value) + "GB";
        }
    }

    /**
     * 创建所有不存在的父级目录
     * destDirName目标目录名
     *
     * @return
     */
    public static Boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (!dir.getParentFile().exists()) {                //判断有没有父路径，就是判断文件整个路径是否存在
            return dir.getParentFile().mkdirs();        //不存在就全部创建
        }
        return false;
    }

    /**
     * 创建当前不存在的一级目录
     * destDirName目标目录名
     *
     * @return
     */
    public static Boolean createCurrentDir(String destDirName) {
        File dir = new File(destDirName);
        if (!dir.exists()) {                //判断有没有父路径，就是判断文件整个路径是否存在
            return dir.mkdirs();        //不存在就全部创建
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
     *                        String
     * @return boolean
     */
    public static void delFile(String filePathAndName) {
        try {
            System.gc();
            java.io.File myDelFile = new java.io.File(filePathAndName);
            if (myDelFile.exists()) {
                myDelFile.setWritable(true);
                myDelFile.delete();
            }
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 读取到字节数组0
     *
     * @param filePath //路径
     * @throws IOException
     */
    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }

    /**
     * 读取到字节数组1
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filePath) throws IOException {

        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 读取到字节数组2
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray2(String filePath) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
     * filePath
     *
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray3(String filePath) throws IOException {

        FileChannel fc = null;
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(filePath, "r");
            fc = rf.getChannel();
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
                    fc.size()).load();
            //System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rf.close();
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 往文件里的内容
     *
     * @param content 写入的内容
     */
    public static void writeFile(String fileP, String content) {
        String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";    //项目路径
        filePath = filePath.replaceAll("file:/", "");
        filePath = filePath.replaceAll("%20", " ");
        filePath = filePath.trim() + fileP.trim();
        if (filePath.indexOf(":") != 1) {
            filePath = File.separator + filePath;
        }
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 往文件里的内容（Projectpath下）
     *
     * @param content 写入的内容
     */
    public static void writeFileCR(String fileP, String content) {
        String filePath = PathUtil.getProjectpath() + fileP;
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文本里的全部内容
     *
     * @param filePath 文件路径
     * @param encoding 编码
     * @return
     */
    public static String readTxtFileAll(String filePath, String encoding) {
        StringBuffer fileContent = new StringBuffer();
        try {
            filePath = filePath.replaceAll("file:/", "");
            filePath = filePath.replaceAll("%20", " ");
            filePath = filePath.trim();
            if (filePath.indexOf(":") != 1) {
                filePath = File.separator + filePath;
            }
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {        // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);    // 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    fileContent.append(lineTxt);
                    fileContent.append("\n");
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
        }
        return fileContent.toString();
    }

    /**
     * 读取Projectpath某文件里的全部内容
     */
    public static String readFileAllContent(String fileP) {
        StringBuffer fileContent = new StringBuffer();
        try {
            String encoding = "utf-8";
            File file = new File(PathUtil.getProjectpath() + fileP);//文件路径
            if (file.isFile() && file.exists()) {        // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);    // 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    fileContent.append(lineTxt);
                    fileContent.append("\n");
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件,查看此路径是否正确:" + fileP);
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
        }
        return fileContent.toString();
    }

    /**
     * 生成新文件名
     *
     * @param prefixPath   文件前缀file
     * @param absolutePath 文件绝对路径/file/demo.txt
     * @return {"fileName":"staff(1).xls","prefix":"staff(1)","suffix":"xls"}
     */
    public static FileUtil.FileObject createNewFileName(String prefixPath, String absolutePath) {

        File file = new File(absolutePath);
        String fileName = file.getName();

        //文件前缀
        String prefix = fileName.substring(0, fileName.lastIndexOf("."));
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

        String oldPrefix = prefix;

        File createFile;

        int i = 1;
        while (true) {
            createFile = new File(prefixPath + fileName);
            if (createFile.isFile() && createFile.exists()) {
                fileName = MessageFormat.format("{0}({1}).{2}", oldPrefix, i, suffix);
                prefix = MessageFormat.format("{0}({1})", oldPrefix, i);
            } else {
                break;
            }
            i++;
        }
        return new FileObject(fileName, prefix, suffix);
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(createNewFileName("e:/up_file/", "e:/up_file/staff.xls")));
    }

    @Data
    @Builder
    public static class FileObject {

        //新文件名
        private String fileName;

        //不带后缀文件名
        private String prefix;

        //文件后缀
        private String suffix;

    }

}
```

* 服务类

```java
/**
 * 通用文件上传接口
 *
 * @param file 文件对象
 * @return
 */
@Override
@Transactional
public FileInfoVO upload(MultipartFile file) {

    String filename = file.getOriginalFilename();
    Assert.hasText(filename, "参数缺失【file】");
    if (!filename.contains(".")) {
        throw new RuntimeException("不支持该文件类型");
        }

        //对应磁盘路径
        String savePath = filePathConfig.getPath();

        //相对路径
        String realPath = Const.REAL_PATH;

        //保存路径
        savePath += realPath + "/";

        //获取文件名、后缀、不带后缀的文件名，如果存在生成新文件名
        FileUtil.FileObject fileObject = FileUtil.createNewFileName(savePath, savePath + filename);

        //文件大小
        long size = 0;


        //创建文件夹
        FileUtil.createCurrentDir(savePath);

        FileOutputStream fos = null;
        FileInputStream fis = null;

        //创建通道
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {

            fos = new FileOutputStream(savePath + fileObject.getFileName());
            fis = (FileInputStream) file.getInputStream();

            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            size = inChannel.size();

            //通道间传输
            inChannel.transferTo(0, size, outChannel);

            inChannel.close();
            outChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PageData saveParam = new PageData();
        saveParam.put("ID", UuidUtil.get32UUID());
        saveParam.put("USER_ID", "");
        saveParam.put("SIZE", FileUtil.getPrintSize(savePath + fileObject.getFileName()));
        saveParam.put("PARENT_ID", "0000"); //文件
        saveParam.put("TYPE", fileObject.getSuffix());
        saveParam.put("FILE_NAME", fileObject.getFileName());
        saveParam.put("FILE_PATH", Const.REAL_PATH);
        saveParam.put("FILE_TYPE", '0'); //0-文件夹,1-文件
        saveParam.put("FILE_SIZE", size);
        saveParam.put("BUSINESS_TYPE", '0'); //0->个人,1->企业,2->部门,3->其他
        saveParam.put("CRE_TIME", new Date());
        saveParam.put("UP_TIME", null);
        saveParam.put("STATE", '0');
        saveParam.put("IS_DEFAULT", '1');
        saveParam.put("DISPLAY_PATH", "");
        saveParam.put("URL", filePathConfig.getHost() + Const.ACCESS_REAL_PATH + Const.REAL_PATH + "/" + fileObject.getFileName());
        fileInfoMapper.insert(saveParam);

        FileInfoVO vo = new FileInfoVO();
        vo.setId(saveParam.getString("ID"));
        vo.setCreTime((Date) saveParam.get("CRE_TIME"));
        vo.setFileName(saveParam.getString("FILE_NAME"));
        vo.setFilePath(saveParam.getString("FILE_PATH"));
        vo.setFileSize(saveParam.getString("FILE_SIZE"));
        vo.setFileType(saveParam.getString("FILE_TYPE"));
        vo.setSize(saveParam.getString("SIZE"));
        vo.setUrl(saveParam.getString("URL"));
        return vo;
    }
```
