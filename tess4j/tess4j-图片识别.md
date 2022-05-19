# tess4j图片识别

> 图片识别英文要准确一点，中文一塌糊涂

* pom.xml

```java

<dependency>
    <groupId>net.sourceforge.tess4j</groupId>
    <artifactId>tess4j</artifactId>
    <version>5.2.0</version>
</dependency>
```

* [字库下载](https://github.com/tesseract-ocr/tessdata)
    * chi_sim.traineddata - 中文
    * eng.traineddata - 英文

> 字库文件可以放到项目下的resources资源目录下

* demo代码

```java

package org.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 参考地址：https://blog.csdn.net/singwhatiwanna/article/details/109152977
 */
public class Main1 {

    public static void main(String[] args) throws URISyntaxException, IOException, TesseractException {

        String path = "D:\\tess4j_img\\a.jpg";

        //语言库位置，也可以设置环境变量TESSDATA_PREFIX必须为TESSDATA_PREFIX，通过System.getProperty("TESSDATA_PREFIX");
        //File tmpFolder = LoadLibs.extractTessResources("win32-x86-64");
        //System.setProperty("java.library.path", tmpFolder.getPath());

        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("chi_sim");
        tesseract.setOcrEngineMode(1);

        //读取项目resource目录下的资源文件data
        Path dataDirectory = Paths.get(ClassLoader.getSystemResource("data").toURI());
        tesseract.setDatapath(dataDirectory.toString());

        File file = new File(path);

        String result = tesseract.doOCR(file);

        System.out.println(result);
    }
}
```
