> 通过插件创建api文档

* 配置gradle
```
apply plugin: "org.grails.grails-doc"

groovydoc {
    docTitle = "首页显示名称"
    windowTitle = "Windows窗口标题"
    // 文件输出路径
    destinationDir = new File('d:/docs')
}

```

* 运行命令

> gradle docs
