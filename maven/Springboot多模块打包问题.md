# 打包报错[找不到启动类]

> `Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:2.2.1.RELEASE:repackage (repackage) on project boss-api: Execution repackage of goal org.springframework.boot:spring-boot-maven-plugin:2.2.1.RELEASE:repackage failed: Unable to find main class`

* 解决方法

> 去掉`pom`依赖文件中的

```xml
<plugin>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

#### 多模块打包

  * 没有启动类（主类）的模块不能添加`spring-boot-maven-plugi`依赖
  * 多模块打包先`clean install`父模块，然后分别对子模块进行`clean install`
