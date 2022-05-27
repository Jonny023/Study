# 常用笔记

```java
// @ApiParam(value = "file")声明文件，可以用@RequestParam或者@RequestPart标识文件
public Response importProject(@ApiParam(value = "file", required = true) @NotNull(message = "file不能为空") @RequestParam MultipartFile file) {
    
}
```
