* yml配置

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 5MB
      max-request-size: 5MB
```

* 异常捕获

```java
@ExceptionHandler(value = MultipartException.class)
public ResultVO fileUploadExceptionHandler(MultipartException e) {
    String msg = "";
    Throwable rootCase = e.getRootCause();
    if (rootCase instanceof org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException) {
        msg = "上传文件过大[文件大小不得超过" + maxFileSize + "]";
    } else if (rootCase instanceof org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException) {
        msg = "文件大小不得超过" + maxFileSize;
    } else {
        msg = "上传失败[服务器异常]";
    }
    logger.error(msg, e);
    return ResultVO.error(ResultEnum.FILE_UPLOAD_ERROR.getCode(), msg);
}

//或者
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UploadHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Map<String, Object> handlerMaxUploadFile(MaxUploadSizeExceededException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        map.put("message", String.format("文件上传限制大小为：%s", maxFileSize));
        return map;
    }
}
```
