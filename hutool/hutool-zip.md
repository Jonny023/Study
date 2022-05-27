# 糊涂工具创建zip

## zip打包下载

```java
package org.example.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static void generateZip(String fileName, List<File> files, HttpServletResponse response) {
        fileName += ".zip";
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            //下载显示的文件名，解决中文名称乱码问题
            String downloadFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
            InputStream inputStream = null;
            for (File file : files) {
                inputStream = FileUtil.getInputStream(file);

                //创建压缩包路径
                zipOutputStream.putNextEntry(new ZipEntry("file/" + file.getName()));

                IoUtil.copy(inputStream, zipOutputStream);
                inputStream.close();
                zipOutputStream.flush();

            }
            zipOutputStream.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 读取zip写入本地磁盘

```java

public static void main(String[] args) {
    String path = "D:\\file\\file.zip";
    ZipFile zipFile = ZipUtil.toZipFile(new File(path), Charset.forName("GBK"));
    //读取指定目录文件列表，根目录传null或""
    List<String> listNames = ZipUtil.listFileNames(zipFile, "json");
    System.out.println(listNames);

    ZipUtil.read(zipFile, (zipEntry) -> {
        final String name = zipEntry.getName();
        InputStream inputStream = ZipUtil.getStream(zipFile, zipEntry);
        FileUtil.writeFromStream(inputStream, new File("d:\\zipOut\\" + name), true);
        System.out.println(name);
    });
    
    //读取zip中指定路径文件
    InputStream inputStream = ZipUtil.get(zipFile, "project.json");
    String jsonString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    System.out.println(jsonString);
    zipFile.close();
}
```
