# 排除不必要的依赖包

* 全局配置 

```groovy
configurations {
//    compile.exclude module: 'commons'
    all*.exclude group: 'org.apache.tomcat', module: 'tomcat-jdbc'
}
```
