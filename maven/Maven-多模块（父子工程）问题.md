# 父子工程问题

> 父工程定义了依赖，子工程无法找到依赖，子模块需要配置<relativePath/>才能获取父类预定义的依赖，否则只能从当前文件中查找

```xml
父模块配置
     <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.12</version>
      </dependency>

子模块 <parent> 没有配置 `<relativePath/>`
如果`<dependencies>`中依赖没有指定版本，编译时不能获取父`<dependencyManagement>`中定义的版本。

```
