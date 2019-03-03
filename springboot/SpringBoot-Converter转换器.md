# `Converter`转换器

* 自定义类实现`Converter`接口可对数据进行自定义处理

> 实现类

```java
package com.example.demo.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @Author Lee
 * @Description
 * @Date 2019年03月03日 15:15
 */
public class TrimConverter implements Converter<String, String> {

    @Override
    public String convert(String s) {
        System.out.println("开始转换....");
        if(s != null) {
            s = s.trim();
        }
        return s;
    }
}
```

> 全局配置类

```java
package com.example.demo.config;

import com.example.demo.converter.TrimConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;

/**
 * @Author Lee
 * @Description
 * @Date 2019年03月03日 15:23
 */
@Configuration
public class TrimConfig {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void addConversionConfig(){
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) requestMappingHandlerAdapter.getWebBindingInitializer();
        if(initializer.getConversionService()!=null){
            GenericConversionService genericConversionService =(GenericConversionService) initializer.getConversionService();
            genericConversionService.addConverter(new TrimConverter());
        }
    }
}
```

> 测试

```
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Lee
 * @Description
 * @Date 2019年03月03日 14:56
 */
@Controller
public class Index {

    @RequestMapping("/{msg}")
    @ResponseBody
    public String index(@PathVariable("msg") String msg) {
        return msg;
    }
}
```

测试方式：localhost:8080/   hello world

# 注意

* 此方式只对控制器方法单个传参方式有效，对`User bean`这种对象参数无效
