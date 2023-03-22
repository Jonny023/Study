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

### idea标签页换行显示

> 打开idea设置》Editor》General》Editor Tabs》Appearance》去掉Show tabs in one row选项


### idea（ctrl+鼠标左键）进入类文件会覆盖窗口，不会在新窗口打开

> 打开idea设置》Editor》General》Editor Tabs》去掉Opening Policy【Enable preview tab】

```bash
Opening Policy
Enable preview tab
The preview tab is reused to show files selected with a single clickin the Project tool window, and files opened during debugging.
```
