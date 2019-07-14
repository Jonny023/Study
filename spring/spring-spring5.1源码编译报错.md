* 环境说明
  * `idea 2018`
  * `jdk1.8_131`
  * `gradle-4.9`

* 错误提示

```
Error:(347, 51) java: 找不到符号
符号: 变量 CoroutinesUtils
位置: 类 org.springframework.core.ReactiveAdapterRegistry.CoroutinesRegistrar
```

* 解决方法，在自定义`module`中加入依赖

```groovy
compile project(":spring-instrument")
compile project(":spring-core-coroutines")
// 或者
compile files("../spring-instrument/build/libs/spring-instrument-5.2.0.BUILD-SNAPSHOT.jar")
compile files("../spring-core-coroutines/build/libs/spring-core-coroutines-5.2.0.BUILD-SNAPSHOT.jar")
```

*  [参考地址](https://github.com/spring-projects/spring-framework/issues/22859)
