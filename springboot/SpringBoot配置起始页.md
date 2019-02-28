# 项目启动后，根路径请求页面

> 方案1：controller里添加一个"/"的映射路径

```java
@RequestMapping("/")
public String index() {
    return "/index";
}
```

> 方案二：设置默认的View跳转页面
* 重写`WebMvcConfigurerAdapter`类

```java
@Configuration
public class DefaultView extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
}
```
