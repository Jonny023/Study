* 用`java`命令行直接执行程序，如果这个程序需要引用`外部jar`包。就不能单纯用`java xx`来执行

* 如果你的`jar`包和程序就在一个目录：

### 编译

```bash
javac -cp D:\yy\yy.jar,D\xx\xx.jar test.java
```

### 执行

```bash
java -cp D:\yy\yy.jar,D\xx\xx.jar test
```bash

* 但是往往我们依赖的包很多，要一个个填写估计就疯了。所有我们一般会把所有的外部依赖包都放在一个文件夹里，比如在`D:\lib`

### 编译 

```bash
javac -Djava.ext.dirs=D:\lib test.java
```bash

### 执行

```bash
java  -Djava.ext.dirs=D:\lib test
```

## 这个方法需要在`jdk1.6`以上支持
