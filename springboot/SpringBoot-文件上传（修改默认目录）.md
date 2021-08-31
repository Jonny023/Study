# SpringBoot文件上传

## 1.配置文件（yaml）

```yaml
upload:
  rootPath: e:\upload
```



> 默认目录为USER_HOME，也就是，若需修改到指定目录，配置如下

```java
package com.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MultipartFileConfig {

    @Value("${upload.rootPath}")
    private String savePath;

    /**
     * 文件上传临时路径
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(savePath);
        return factory.createMultipartConfig();
    }
}
```

## 2.资源映射

```java
package com.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Value("${upload.rootPath}")
    private String rootPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imagesResourceLocation = "file:" + deployRootPath + File.separator + "img" + File.separator;
        registry.addResourceHandler("/img/**").addResourceLocations(imagesResourceLocation);
    }
    
}
```

## 3.上传服务类

```java
package com.service.impl;

import com.service.IImageService;
import com.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@Service
public class ImageServiceImpl implements IImageService {

    @Value("${upload.rootPath}")
    private String savePath;

    private Logger LOG = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public String upload(MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new RuntimeException("文件不能为空！");
        }

        try {
            if (!ImageUtil.validateImage(multipartFile.getBytes())) {
                throw new RuntimeException("只能上传图片！");
            }
        } catch (IOException e) {
            LOG.error("{}", e);
        }

        String originalFilename = multipartFile.getOriginalFilename();
        LOG.info("原文件名：{}", originalFilename);
        LocalDate now = LocalDate.now();
        String parentPath = String.format("/img/%s/%s/%s", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        String realPath = String.format("%s/%s", parentPath, originalFilename);
        File newFile = new File(savePath + realPath);

        if (!newFile.exists()) {
            newFile.getParentFile().mkdirs();
        }

        try (InputStream is = multipartFile.getInputStream();
             FileOutputStream fos = new FileOutputStream(newFile)) {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            return realPath;
        } catch (IOException e) {
            LOG.error("{}", e);
            throw new RuntimeException("文件错误！");
        }

    }
}
```

## 4.控制层

```java
package com.controller;

import com.common.Result;
import com.service.IImageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Resource
    private IImageService imageService;

    @PostMapping("/upload")
    public Result upload(@RequestPart(name = "file") MultipartFile multipartFile) {
        return Result.ok(imageService.upload(multipartFile));
    }
}
```

## 5.图片验证工具类

```java
package com.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {

    private static Logger LOG = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 校验上传的东西是否为图片 GIF/JPG/PNG/BMP
     *
     * @param imgContent
     * @return
     */
    public static boolean validateImage(byte[] imgContent) {
        try {
            int len = imgContent.length;
            byte n1 = imgContent[len - 2];
            byte n2 = imgContent[len - 1];
            byte b0 = imgContent[0];
            byte b1 = imgContent[1];
            byte b2 = imgContent[2];
            byte b3 = imgContent[3];
            byte b4 = imgContent[4];
            byte b5 = imgContent[5];
            byte b6 = imgContent[6];
            byte b7 = imgContent[7];
            byte b8 = imgContent[8];
            byte b9 = imgContent[9];

            // GIF(G I F 8 7 a)
            if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F' && b3 == (byte) '8' && b4 == (byte) '7' && b5 == (byte) 'a') {
                return true;
                // GIF(G I F 8 9 a)
            } else if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F' && b3 == (byte) '8' && b4 == (byte) '9' && b5 == (byte) 'a') {
                return true;
                // PNG(89 P N G 0D 0A 1A)
            } else if (b0 == -119 && b1 == (byte) 'P' && b2 == (byte) 'N' && b3 == (byte) 'G' && b4 == 13 && b5 == 10 && b6 == 26) {
                return true;
                // JPG JPEG(FF D8 --- FF D9)
            } else if (b0 == -1 && b1 == -40 && n1 == -1 && n2 == -39) {
                return true;
            } else if (b6 == (byte) 'J' && b7 == (byte) 'F' && b8 == (byte) 'I' && b9 == (byte) 'F') {
                return true;
            } else if (b6 == (byte) 'E' && b7 == (byte) 'x' && b8 == (byte) 'i' && b9 == (byte) 'f') {
                return true;
                // BMP(B M)
            } else if (b0 == (byte) 'B' && b1 == (byte) 'M') {
                return true;
            } else {
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            LOG.error("UploadFileUtil.检查文件类型错误：{}", e);
            return false;
        }
    }
}
```

