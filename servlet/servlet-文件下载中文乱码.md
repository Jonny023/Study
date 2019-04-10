# 代码

```java
package com.common.file;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 文件下载浏览器编码工具
 */
public class FileBrowserUtil {

    /**
     * 根据不同浏览器 User-Agent，生成不同的 Content_disposition
     *
     * @param fileName
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getContentDisposition(String fileName, HttpServletRequest request) throws UnsupportedEncodingException {
        String content_disposition = "";
        String userAgent = request.getHeader("User-Agent");
        // 针对IE或者以IE为内核的浏览器：  
//        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
//            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
//        } else {
//            // 非IE浏览器的处理：  
//            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
//        }
        fileName= new String(fileName.getBytes("ISO-8859-1"), "utf-8"); // 转中文乱码
        fileName= URLEncoder.encode(fileName, "utf-8"); // 符合 RFC 6266 标准
        content_disposition = String.format("attachment;filename=\"%s\";%s", fileName,";filename*=utf-8''"+fileName);
        return content_disposition;
    }
}

```
