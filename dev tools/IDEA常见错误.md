* `Error running 'BmaApplication': Command line is too long. Shorten command line for BmaApplication or also for Spring Boot default configuration?`

```bash
将Edit Configurations/Environment/Shorten command line设置为：JAE manifest - java -cp classpath.jar className [args]
```


* 控制台中文乱码

> Help - Edit Custom VM Options添加

```bash
-Dfile.encoding=UTF-8
```
