# SpringBoot获取资源目录resources下的文件

> 文件存放到`resources/db`下，如：`resource/db/ip2region.db`，试过其他方式要么报错，要么打包jar后报错

```java
ClassPathResource resource = new ClassPathResource("db/ip2region.db");
System.out.println(resource.getFile().getPath());
System.out.println(resource.getInputStream());
```

## 读取资源文件、下载

```java
package com.example.springbootresource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


@RestController
public class MainController {

    /**
     * 读取resources目录下的资源文件
     *
     * @return
     * @throws IOException
     */
    @GetMapping("/")
    public String index() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //读取多个资源文件
        Resource[] resources = resolver.getResources("a.txt");
        Resource resource = resources[0];
        //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
        InputStream stream = resource.getInputStream();
        StringBuilder buffer = new StringBuilder();
        byte[] bytes = new byte[1024];
        try {
            for (int n; (n = stream.read(bytes)) != -1; ) {
                buffer.append(new String(bytes, 0, n, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 读取resources资源并下载
     * @param response
     */
    @GetMapping("/download")
    public void index(HttpServletResponse response) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String fileName = "a.txt";
        //读取单个资源文件，不存在则报错
        Resource resource = resolver.getResource(fileName);

        //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
        try (InputStream inputStream = resource.getInputStream()) {

            // 设置输出的格式
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            //response.addHeader("Content-Length", "" + length);
            response.setContentType("application/octet-stream");

            int len = 0;
            //创建数据缓冲区
            byte[] buffer = new byte[10240];
            //通过response对象获取outputStream流
            //将FileInputStream流写入到buffer缓冲区
            try (OutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
                while ((len = inputStream.read(buffer)) > 0) {
                    //使用OutputStream将缓冲区的数据输出到浏览器
                    outputStream.write(buffer, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```



## springboot打包jar后访问资源文件

> 经常遇到springboot打包jar后，因业务需要，比如文件上传模板存放到resources资源目录，本地开发环境能访问，但是打包jar部署到线上时却用不了了，下面是几种打包jar后也能访问的几种方式

```java
package org.example.controller;

import org.example.utils.DownloadUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {

    @javax.annotation.Resource
    private ResourceLoader resourceLoader;
    @javax.annotation.Resource
    private ApplicationContext applicationContext;

    @GetMapping("/1")
    public void file1() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file/1.txt");
        DownloadUtil.download(inputStream, "1.txt");
    }

    @GetMapping("/2")
    public void file2() {
        InputStream inputStream = getClass().getResourceAsStream("/file/2.txt");
        DownloadUtil.download(inputStream, "2.txt");
    }

    @GetMapping("/3")
    public void file3() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/file/3.txt");
        InputStream inputStream = resource.getInputStream();
        DownloadUtil.download(inputStream, "3.txt");
    }

    @GetMapping("/4")
    public void file4() throws IOException {
        Resource resource = applicationContext.getResource("classpath:file/4.txt");
        InputStream inputStream = resource.getInputStream();
        DownloadUtil.download(inputStream, "4.txt");
    }

    @GetMapping("/5")
    public void file5() throws IOException {
        ClassPathResource resource = new ClassPathResource("file/5.txt");
        DownloadUtil.download(resource.getInputStream(), "5.txt");
    }
}
```
