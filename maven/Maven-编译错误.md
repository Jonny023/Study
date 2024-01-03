No.1 程序包 com.sun.istack.internal 不存在

> pom文件新增

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <compilerArgs>
                    <arg>-XDignore.symbol.file</arg>
                </compilerArgs>
                <fork>true</fork>
            </configuration>
        </plugin>
    </plugins>
</build>
```



* No.2 运行报错

```
java: Internal error in the mapping processor: java.lang.NullPointerException  	at org.mapstruct.ap.internal.processor.DefaultVersionInformation.createManifestUrl(DefaultVersionInformation.java:182)  	at org.mapstruct.ap.internal.processor.DefaultVersionInformation.openManifest(DefaultVersionInformation.java:153)  	at org.mapstruct.ap.internal.processor.DefaultVersionInformation.getLibraryName(DefaultVersionInformation.java:129)  	at org.mapstruct.ap.internal.processor.DefaultVersionInformation.getCompiler(DefaultVersionInformation.java:122)  	at org.mapstruct.ap.internal.processor.DefaultVersionInformation.fromProcessingEnvironment(DefaultVersionInformation.java:95)  	at org.mapstruct.ap.internal.processor.DefaultModelElementProcessorContext.<init>(DefaultModelElementProcessorContext.java:50)  	at org.mapstruct.ap.MappingProcessor.processMapperElements(MappingProcessor.java:218)  	at org.mapstruct.ap.MappingProcessor.process(MappingProcessor.java:156)  	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)  	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)  	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)  	at java.lang.reflect.Method.invoke(Method.java:498)  	at org.jetbrains.jps.javac.APIWrappers$1.invoke(APIWrappers.java:248)  	at org.springframework.boot.configurationprocessor.ConfigurationMetadataAnnotationProcessor.process(Unknown Source)  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.callProcessor(JavacProcessingEnvironment.java:794)  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.discoverAndRunProcs(JavacProcessingEnvironment.java:705)  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.access$1800(JavacProcessingEnvironment.java:91)  	at com.sun.tools.javac.processing.JavacProcessingEnvironment$Round.run(JavacProcessingEnvironment.java:1035)  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.doProcessing(JavacProcessingEnvironment.java:1176)  	at com.sun.tools.javac.main.JavaCompiler.processAnnotations(JavaCompiler.java:1170)  	at com.sun.tools.javac.main.JavaCompiler.compile(JavaCompiler.java:856)  	at com.sun.tools.javac.main.Main.compile(Main.java:523)  	at com.sun.tools.javac.api.JavacTaskImpl.doCall(JavacTaskImpl.java:129)  	at com.sun.tools.javac.api.JavacTaskImpl.call(JavacTaskImpl.java:138)  	at org.jetbrains.jps.javac.JavacMain.compile(JavacMain.java:231)  	at org.jetbrains.jps.incremental.java.JavaBuilder.compileJava(JavaBuilder.java:501)  	at org.jetbrains.jps.incremental.java.JavaBuilder.compile(JavaBuilder.java:353)  	at org.jetbrains.jps.incremental.java.JavaBuilder.doBuild(JavaBuilder.java:277)  	at org.jetbrains.jps.incremental.java.JavaBuilder.build(JavaBuilder.java:231)  	at org.jetbrains.jps.incremental.IncProjectBuilder.runModuleLevelBuilders(IncProjectBuilder.java:1441)  	at org.jetbrains.jps.incremental.IncProjectBuilder.runBuildersForChunk(IncProjectBuilder.java:1100)  	at org.jetbrains.jps.incremental.IncProjectBuilder.buildTargetsChunk(IncProjectBuilder.java:1224)  	at org.jetbrains.jps.incremental.IncProjectBuilder.buildChunkIfAffected(IncProjectBuilder.java:1066)  	at org.jetbrains.jps.incremental.IncProjectBuilder.buildChunks(IncProjectBuilder.java:832)  	at org.jetbrains.jps.incremental.IncProjectBuilder.runBuild(IncProjectBuilder.java:419)  	at org.jetbrains.jps.incremental.IncProjectBuilder.build(IncProjectBuilder.java:183)  	at org.jetbrains.jps.cmdline.BuildRunner.runBuild(BuildRunner.java:132)  	at org.jetbrains.jps.cmdline.BuildSession.runBuild(BuildSession.java:302)  	at org.jetbrains.jps.cmdline.BuildSession.run(BuildSession.java:132)  	at org.jetbrains.jps.cmdline.BuildMain$MyMessageHandler.lambda$channelRead0$0(BuildMain.java:219)  	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)  	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)  	at java.lang.Thread.run(Thread.java:748)
```

> 解决方法

* Build--Compiler--local vm option...添加配置
  * `-Djps.track.ap.dependencies=false`
  



* No.3 多环境动态指定插件开发环境没问题，线上正式环境出问题

> 原因及解决方法：类配置加了`@Profile("dev")`配置，打包的时候多个组件需要指定`profiles`

```bash
java -jar demo.jar --spring.profiles.active=dev,swagger
```

### 打包报错

* maven打包报错：

```sh
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.8.1:compile (default-compile) on project hui-api-busi-module-trade: Compilation failure -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
[ERROR] 
[ERROR] After correcting the problems, you can resume the build with the command
[ERROR]   mvn <goals> -rf :hui-api-busi-module-trade

Process finished with exit code 1
```

#### 解决方案

* 删除java类中未使用的引用

[参考地址](https://blog.csdn.net/weixin_68696833/article/details/125313895)
