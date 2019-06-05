#### 测试时`metadata-extracto`用`2.6.2`，测试`png`文件报错（不支持png），所以后来换成了最新版

* 依赖

> `Gradle依赖`

```
compile 'com.drewnoakes:metadata-extractor:2.11.0'
compile group: 'com.adobe.xmp', name: 'xmpcore', version: '5.1.3'
```

> `Maven`依赖

```xml
<dependency>
    <groupId>com.adobe.xmp</groupId>
    <artifactId>xmpcore</artifactId>
    <version>5.1.3</version>
</dependency>
<dependency>
    <groupId>com.drewnoakes</groupId>
    <artifactId>metadata-extractor</artifactId>
    <version>2.11.0</version>
</dependency>

```

* 测试代码

```java
package com.demo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;

public class FileTest {

    public static void main(String[] args) throws ImageProcessingException, IOException {
        File jpegFile = new File("e:/temp.jpg");
        System.out.println(jpegFile.isFile());
        Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
        String longitude = ""; // 精度
        String latitude = ""; // 纬度
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.format("[%s] - %s = %s\n", directory.getName(), tag.getTagName(), tag.getDescription());

                String tagName = tag.getTagName();  //标签名
                String desc = tag.getDescription(); //标签信息
                if (tagName.equals("GPS Latitude")) {
                    longitude=desc;
                } else if (tagName.equals("GPS Longitude")) {
                    latitude=desc;
                }
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }
        System.out.printf("精度：%s \n", longitude);
        System.out.printf("纬度：%s \n", latitude);
    }
}

```
