# Maven常用命令



* 常用打包命令

```bash
# 清理缓存并打包
mvn clean package

# 编译指定模块
mvn clean compile -pl a,b

# 跳过指定模块发布
mvn clean compile -pl !a,!b
mvn clean install -pl "!a,!b"

# 指定环境打包
mvn -DskipTests=true package -P dev
```



* 命令作用对照表

| 命令       | 作用                                                 |
| ---------- | ---------------------------------------------------- |
| `clean`    | 清理本地编译的文件，`classes`目录                    |
| `install`  | 编译安装（发布）到本地`maven`仓库                    |
| `package`  | 打包，将工程文件打包为指定的格式，例如`JAR`，`WAR`等 |
| `validate` | 验证，验证工程是否正确，所需的信息是否完整           |
| `compile`  | 编译源码，编译生成`class`文件                        |
| `deploy`   | 发布到远程仓库                                       |
| `build`    | 功能类似`compile`，针对整个工程进行编译              |

### maven多模块解释

* 多模块项目直接在父工程模块`package`不会报错

* 多模块单独对子模块打包时，如果这个子模块还有依赖其他子模块，其他子模块必须先要编译`classes`，才能对这个子模块进行`clean package`
* 如果多模块是通过`mvn install`将多个模块发布到本地仓库，之后对子模块单独打包不会报错，但是有个缺点，如果修改了代码，如果不再次`mvn install`发布，其他模块引用的还是这个模块之前发布的内容，可能就会有疑问，我明明修改了代码，而且编译了，为何没有修改到（没有生效）？
* 如果是`mvn package`方式依赖项目，必须保证被依赖的模块要预先编译，否则项目无法编译通过