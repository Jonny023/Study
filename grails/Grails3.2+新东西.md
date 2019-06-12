# `Grails3.2+新增了一些新东西`

> 记录一下一些坑

* `Grails3.2-`版可以直接在`domain`中注入`service`，在`Grails3.2+`中默认关闭了自动注入功能

### 开启方式

* 方式一 

> 在`application.groovy`中配置全局映射，若没有此脚本需新建

```groovy
grails.gorm.default.mapping = {
    autowire true
}
```

* 方式二

> 在需要注入`bean`的`domain`中添加映射

```groovy
static mapping = {
    autowire true
}
```

* 方式三

> 在`application.yml`中新增或修改配置

```yaml
---
grails:
    gorm:
        autowire: true
```

## [参考](http://gorm.grails.org/latest/hibernate/manual/index.html#_domain_autowiring_disabled_by_default)
