# SpringCloud常见问题

## No.1

> 加入nacos配置中心后，maven打包报错，错误提示：`org.yaml.snakeyaml.error.YAMLException: java.nio.charset.MalformedInputException: Input length = 1`,原因是nacos配置中心的
> 配置包含有中文字符注释，解决方法如下，启动命令添加编码参数

```sh
java -Dfile.encoding=utf-8 -jar test.jar
```

