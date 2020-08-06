> 依赖

```xml
<!--项目中已使用poi，此处排除-->
<dependency>
    <groupId>org.jxls</groupId>
    <artifactId>jxls-poi</artifactId>
    <version>1.1.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
        </exclusion>
    </exclusions>
</dependency>
        
<!--POI-->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.17</version>
    </dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.17</version>
</dependency>
```

> 代码

```java
package com.gd.common.service;

import lombok.SneakyThrows;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author Jonny
 * @description
 * @date 2020/8/6 16:01
 */
@Service
public class ExcelToolService {

    /**
     * 生成excel文件
     *
     * @param request
     * @param datas    数据集合
     * @param template 模板文件：annex.xls，存放到resources下
     * @param fileName 生成后的文件名，不带后缀
     * @return
     */
    @SneakyThrows
    public void create(HttpServletRequest request, List datas, String template, String fileName) {

        ServletContext servletContext = request.getSession().getServletContext();
        String path = servletContext.getRealPath("/excelTemp");
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        String filePath = path + File.separator + fileName + ".xls";
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        try (
                FileInputStream inputStream = new FileInputStream(servletContext.getRealPath("/excelTemplates/") + template);
                FileOutputStream os = new FileOutputStream(filePath)) {
            Context context = new PoiContext();
            context.putVar("datas", datas);
            JxlsHelper.getInstance().processTemplate(inputStream, os, context);
        }
    }
}
```

