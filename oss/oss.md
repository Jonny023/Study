## oss文件删除

> 文件删除时路径最前面不能有斜杠“/”，不然无法删除

```java
/**

/**
 * 获取阿里云OSS客户端对象
 *
 * @return ossClient
 */
@Bean
public OSS getOSSClient() {
    return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
}

/*
 * 根据key删除OSS服务器上的文件
 *
 * @param filePath   文件路径app/xxx/xxx.png 文件路径最前面不能有/
 */
public void deleteFile(String filePath) {
    oss.deleteObject(BACKET_NAME, filePath);
    log.info("删除{}下的文件{}成功", BACKET_NAME, filePath);
}
```
