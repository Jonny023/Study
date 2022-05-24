# 糊涂工具创建zip

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
                zipOutputStream.flush();

            }
            zipOutputStream.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
