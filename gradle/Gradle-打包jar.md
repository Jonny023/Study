## Gradle打包指定类到jar

* build.gradle中配置
```java
jar {
    manifestContentCharset 'utf-8'
    metadataCharset 'utf-8'
    manifest {
        attributes "Main-Class": "com.jonny.CommonUtil"
    }
    from {
        configurations.compile.collect { it.isDirectory() ? it : zipTree(it) }
    }
}
```

* 运行jar中指定类main函数

```bash
java -jar customjar.jar

java -classpath customjar.jar com.jonny.CommonUtil

java -cp customjar.jar com.jonny.CommonUtil
```