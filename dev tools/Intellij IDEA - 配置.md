## 转移系统配置文件存放路径

* 打开`bin/idea.properties`，添加配置

```properties
# 配置idea配置存放路径
idea.config.path=F:/IDEAConfig/config
idea.system.path=F:/IDEAConfig/system
idea.plugins.path=F:/IDEAConfig/plugins
idea.log.path=F:/IDEAConfig/log
```

## idea编译中文出现乱码解决
> `ctrl+alt+s`--`Build,Exection,Deployedment--Compiler--Additional command line parameters`, 在中加入

```bash
-encoding utf-8
```

## idea中文乱码

```prpperties
-Dfile.encoding=UTF-8
```

## 类注释

> 类名通过`/**`自动添加备注

```java
**
 * @author xxx E-mail:1234@qq.com
 * @date 创建时间：$DATE$ $TIME$
 * @description
 */
```

> 
