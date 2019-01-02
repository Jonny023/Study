> jdk配置步骤
* 解压或安装至本地
* 新建JAVA_HOME  值：c:\jdk path
* path中添加%JAVA_HOME%\bin;
* 要想javac可用，则需配置：新建CLASSPATH  值：.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar

> 查看环境是否可用以及版本号
```bash
java -version
javac -version
```
> 查看jdk安装路径信息
```bash
java -XshowSettings:properties -version
```

> 编译java文件
*  错误: 编码GBK的不可映射字符
  `javac -encoding UTF-8 Test.java`

```bash
javac Test.java
```

> 运行

```bash
java Test
```
