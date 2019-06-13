> 多个版本相同，但不冲突，可通过如下配置强制使用同一版本

```groovy
configurations.all {
    resolutionStrategy {
        force 'commons-io:commons-io:2.4'
    }
}
```
