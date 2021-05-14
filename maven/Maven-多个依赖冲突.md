# 依赖冲突

> 项目依赖的jar存在多个，指定版本覆盖

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>${knife4j.version}</version>
    <exclusions>
        <exclusion>
            <artifactId>springfox-swagger2</artifactId>
            <groupId>io.springfox</groupId>
        </exclusion>
        <exclusion>
            <artifactId>swagger-models</artifactId>
            <groupId>io.swagger</groupId>
        </exclusion>
        <exclusion>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-bean-validators</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<!-- 排除后自定指定版本覆盖 -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>${springfox-swagger2.version}</version>
    <exclusions>
        <exclusion>
            <groupId>io.swagger</groupId>
            <artifactId>swagger-annotations</artifactId>
        </exclusion>
        <exclusion>
            <groupId>io.swagger</groupId>
            <artifactId>swagger-models</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-annotations</artifactId>
    <version>${swagger.version}</version>
</dependency>
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-models</artifactId>
    <version>${swagger.version}</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-bean-validators</artifactId>
    <version>${springfox-bean-validators.version}</version>
</dependency>
```

