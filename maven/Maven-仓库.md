## Maven仓库

### 阿里云

```xml
<repositories>
    <repository>
        <id>central</id>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <layout>default</layout>
        <!-- 是否开启发布版构件下载 -->
        <releases>
            <enabled>true</enabled>
        </releases>
        <!-- 是否开启快照版构件下载 -->
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

### maven镜像配置

> 就算配置了多个镜像地址，也只会有一个生效，如果在项目中无法生效，则需要在pom.xml中声明一下

```xml
<mirrors>
    <mirror>  
        <id>alimaven</id>  
        <mirrorOf>central</mirrorOf>
        <name>aliyun maven</name>  
        <url>https://maven.aliyun.com/nexus/content/groups/public/</url>
    </mirror>
    <mirror>  
        <id>huaweicloud</id>  
        <mirrorOf>central</mirrorOf>
        <name>huaweiyun maven</name>  
        <url>https://mirrors.huaweicloud.com/repository/maven/</url>
    </mirror>
    <mirror>  
        <id>mvnrepository</id>  
        <mirrorOf>central</mirrorOf>
        <name>mvnrepository maven</name>  
        <url>https://mvnrepository.com/artifact/</url>
    </mirror>
    <mirror>    
        <id>maven2</id> 
        <mirrorOf>central</mirrorOf>    
        <name>Human Readable Name for this Mirror.</name>    
        <url>http://uk.maven.org/maven2/</url>    
    </mirror>
    <mirror>    
      <id>ibiblio</id>    
      <mirrorOf>central</mirrorOf>    
      <name>Human Readable Name for this Mirror.</name>    
      <url>http://mirrors.ibiblio.org/pub/mirrors/maven2/</url>    
    </mirror>
    
  </mirrors>
```

