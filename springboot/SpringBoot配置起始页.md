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
* `2.0`用`WebMvcConfigurationSupport `或`WebMvcConfigurer `代替`WebMvcConfigurerAdapter`

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

## 常用接口

```java
/** 解决跨域问题 **/
public void addCorsMappings(CorsRegistry registry) ;

/** 添加拦截器 **/
void addInterceptors(InterceptorRegistry registry);

/** 这里配置视图解析器 **/
void configureViewResolvers(ViewResolverRegistry registry);

/** 配置内容裁决的一些选项 **/
void configureContentNegotiation(ContentNegotiationConfigurer configurer);

/** 视图跳转控制器 **/
void addViewControllers(ViewControllerRegistry registry);

/** 静态资源处理 **/
void addResourceHandlers(ResourceHandlerRegistry registry);

/** 默认静态资源处理器 **/
void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer);
```
