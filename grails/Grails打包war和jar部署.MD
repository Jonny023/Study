# 一、grails打包tomcat本地部署和jar部署

[参考](https://docs.grails.org/latest/guide/deployment.html)

## 1. war包本地tomcat部署

```groovy
apply plugin:"war"

dependencies {
    provided "org.springframework.boot:spring-boot-starter-tomcat"
}
```

## 2. jar包部署
> 去掉依赖中的war插件

```groovy
//apply plugin:"war"

dependencies {
    provided "org.springframework.boot:spring-boot-starter-tomcat"
}
```

# 

# 二、IDEA中创建grails的rest-api后，如果不在urlMapping.groovy添加映射无法访问控制器

> 默认生成的mapping如下

```groovy
package test

class UrlMappings {

    static mappings = {

        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
```

> 如果不想添加`mapping`，通过默认的控制器名称+action方法名访问，添加如下配置

```groovy
package test

class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
```

```groovy
package com.test

import grails.converters.*

class HelloController {

    def index() {
        render "this is index."
    }

    def hi() {
        def map = [name: 'Jonny', age: 20]
        render map as JSON
    }
}
```

> 如果不添加`mapping`配置，上面的`HelloController`通过`/hello/hi`访问报错404，因为`mapping`只配置了`get`方法默认访问`index`方法

