# 父子工程问题

## No.1 

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

## No.2 多模块打包报错

> 父pom.xml声明了modules，引入多个子module，在父工程下执行`mvn clean package`报错，因为各个模块之间有依赖关系，想要直接执行打包操作
> 首先注释掉父pom.xml中的modules标签，然后执行`mvn clean install`，完成后在解开modules注释即可正常执行`mvn clean package`，如：

```xml
<modules>
   <module>../XXX-commons</module>
   <module>../XXX-server</module>
   <module>../XXX-auth</module>
   <module>../XXX-user</module>
</modules>
```
