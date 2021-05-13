# Windows开机自启软件

[参考地址](https://blog.csdn.net/wanghuiict/article/details/73196222)

* 第一步：打开开机启动目录

```bash
# 共用目录，应该要管理员权限才能运行
shell:common startup

# 当前登陆用户目录，脚本放在此目录，对当前登陆用户有效
shell:startup
```

> 第二步：在第一步打开的目录里面创建一个bat脚本，名字随便定，如：startIDEA.bat

* start命令里面的`""`引号作用
	* 为了防止cmd打开新窗口而无法运行exe
	* 防止空格导致无法找到文件

> 文件中写入要启动软件的exe文件路径，也可以拖动快捷方式到这个目录中（需要管理员权限）

```bash
# 方式一（路径有空格）
start "" "C:\Users\user\AppData\Local\JetBrains\IntelliJ IDEA 2021.1\bin\idea64.exe"

# 方式二（路径有空格）
start /D "C:/Users/user/AppData/Local/JetBrains/IntelliJ IDEA 2021.1/bin" "" "C:/Users/user/AppData/Local/JetBrains/IntelliJ IDEA 2021.1/bin/idea64.exe"

# 方式三（路径无空格）
start c:/file/run.exe
```

* 测试，注销重启看是否生效

