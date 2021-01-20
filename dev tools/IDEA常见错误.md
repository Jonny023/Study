* `Error running 'Application': Command line is too long. Shorten command line for Application or also for Spring Boot default configuration?`

> 启动项目报错

```bash
将Edit Configurations/Environment/Shorten command line设置为：JAE manifest - java -cp classpath.jar className [args]
```


* 控制台中文乱码

> Help - Edit Custom VM Options添加

```bash
-Dfile.encoding=UTF-8
```
