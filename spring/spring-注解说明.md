# Spring注解

### 1.声明bean的注解

* `@Component` 组件，没有明确的角色
* `@Service` 在业务逻辑层使用（service层）
* `@Repository` 在数据访问层使用（dao层）
* `@Controller` 在展现层使用，控制器的声明（C）

### 2.注入bean的注解

* `@Autowired`：由`Spring`提供
* `@Inject`：由`JSR-330`提供
* `@Resource`：由`JSR-250`提供

* 都可以注解在set方法和属性上，推荐注解在属性上（一目了然，少写代码）。

### 3.java配置类相关注解

* `@Configuration` 声明当前类为配置类，相当于`xml`形式的`Spring`配置（类上）
* `@Bean` 注解在方法上，声明当前方法的返回值为一个`bean`，替代`xml`中的方式（方法上）
* `@Configuration` 声明当前类为配置类，其中内部组合了`@Component`注解，表明这个类是一个`bean`（类上）
* `@ComponentScan` 用于对`Component`进行扫描，相当于`xml`中的（类上）
* `@WishlyConfiguration` 为`@Configuration`与`@ComponentScan`的组合注解，可以替代这两个注解

### 4.切面（AOP）相关注解

> Spring支持AspectJ的注解式切面编程。

`@Aspect` 声明一个切面（类上）

* 使用`@After、@Before、@Around`定义建言（advice），可直接将拦截规则（切点）作为参数。

* `@After` 在方法执行之后执行（方法上）
* `@Before` 在方法执行之前执行（方法上）
* `@Around` 在方法执行之前与之后执行（方法上）
* `@PointCut` 声明切点

* 在java配置类中使用@EnableAspectJAutoProxy注解开启Spring对AspectJ代理的支持（类上）*

### 5.@Bean的属性支持

* `@Scope` 设置`Spring`容器如何新建`Bean`实例（方法上，得有`@Bean`）其设置类型包括：

  * `Singleton` （单例,一个Spring容器中只有一个bean实例，默认模式）,
  * `Protetype` （每次调用新建一个bean）,
  * `Request` （web项目中，给每个http request新建一个bean）,
  * `Session` （web项目中，给每个http session新建一个bean）,
  * `GlobalSession`（给每一个 global http session新建一个Bean实例）
  * `@StepScope` 在`Spring Batch`中还有涉及
  * `@PostConstruct` 由`JSR-250`提供，在构造函数执行完之后执行，等价于xml配置文件中bean的initMethod
  * `@PreDestory` 由JSR-250提供，在Bean销毁之前执行，等价于xml配置文件中bean的destroyMethod

### 6.@Value注解

`@Value` 为属性注入值（属性上）

* 支持如下方式的注入：

> 注入普通字符

```java
@Value("Michael Jackson")
String name;

```

> 注入操作系统属性

```java
@Value("#{systemProperties['os.name']}")
String osName;

```

> 注入表达式结果

```java
@Value("#{ T(java.lang.Math).random() * 100 }")
String randomNumber;

```

> 注入其它bean属性

```java
@Value("#{domeClass.name}")
String name;

```

> 注入文件资源

```java
@Value("classpath:com/hgs/hello/test.txt")
String Resource file;

```
> 注入网站资源

```java
@Value("http://www.cznovel.com")
Resource url;

```

> 注入配置文件

```java
@Value("${book.name}")
String bookName;

```

> 注入配置使用方法：

*  编写配置文件（test.properties）

```properties
book.name=《三体》
```

> @PropertySource 加载配置文件(类上)

```java
@PropertySource("classpath:com/hgs/hello/test/test.propertie")

```

* 还需配置一个`PropertySourcesPlaceholderConfigure`r的`bean`。

### 7.环境切换

* `@Profile` 通过设定`Environment`的`ActiveProfiles`来设定当前`context`需要使用的配置环境。（类或方法上）

* `@Conditional` `Spring4`中可以使用此注解定义条件话的`bean`，通过实现`Condition`接口，并重写`matches`方法，从而决定该`bean`是否被实例化。（方法上）


### 8.异步相关

* `@EnableAsync` 配置类中，通过此注解开启对异步任务的支持，叙事性AsyncConfigurer接口（类上）

* `@Async` 在实际执行的bean方法使用该注解来申明其是一个异步任务（方法上或类上所有的方法都将异步，需要`@EnableAsyn`c开启异步任务）


### 9.定时任务相关

* `@EnableScheduling` 在配置类上使用，开启计划任务的支持（类上）

* `@Scheduled` 来申明这是一个任务，包括`cron,fixDelay,fixRate`等类型（方法上，需先开启计划任务的支持）


### 10.@Enable*注解说明

* 这些注解主要用来开启对xxx的支持。

* `@EnableAspectJAutoProxy` 开启对`AspectJ`自动代理的支持

* `@EnableAsync` 开启异步方法的支持

* `@EnableScheduling` 开启计划任务的支持

* `@EnableWebMvc` 开启`Web MVC`的配置支持

* `@EnableConfigurationProperties` 开启对`@ConfigurationProperties`注解配置`Bean`的支持

* `@EnableJpaRepositories` 开启对`SpringData JPA Repository`的支持

* `@EnableTransactionManagement` 开启注解式事务的支持

* `@EnableTransactionManagement` 开启注解式事务的支持

* `@EnableCaching` 开启注解式的缓存支持

### 11.测试相关注解

* `@RunWith` 运行器，`Spring`中通常用于对`JUnit`的支持
* `@RunWith(SpringJUnit4ClassRunner.class)`
* `@ContextConfiguration` 用来加载配置`ApplicationContext`，其中`classes`属性用来加载配置类
* `@ContextConfiguration(classes={TestConfig.class})`

#### SpringMVC部分

* `@EnableWebMvc` 在配置类中开启`Web MVC`的配置支持，如一些`ViewResolver`或者`MessageConverter`等，若无此句，重写`WebMvcConfigurerAdapter`方法（用于对`SpringMVC`的配置）。

* `@Controller` 声明该类为`SpringMVC`中的`Controller`

* `@RequestMapping` 用于映射Web请求，包括访问路径和参数（类或方法上）

* `@ResponseBody` 支持将返回值放在`response`内，而不是一个页面，通常用户返回`json`数据（返回值旁或方法上）

* `@RequestBody` 允许`request`的参数在`request`体中，而不是在直接连接在地址后面。（放在参数前）

* `@PathVariable` 用于接收路径参数，比如`@RequestMapping(“/hello/{name}”)`申明的路径，将注解放在参数中前，即可获取该值，通常作为`Restful`的接口实现方法。

* `@RestController` 该注解为一个组合注解，相当于`@Controller`和`@ResponseBody`的组合，注解在类上，意味着，该`Controller`的所有方法都默认加上了`@ResponseBody`。

* `@ControllerAdvice` 通过该注解，我们可以将对于控制器的全局配置放置在同一个位置，注解了`@Controller`的类的方法可使用`@ExceptionHandler、@InitBinder、@ModelAttribute`注解到方法上，
这对所有注解了 `@RequestMapping`的控制器内的方法有效。

* `@ExceptionHandler` 用于全局处理控制器里的异常

* `@InitBinder` 用来设置`WebDataBinder，WebDataBinder`用来自动绑定前台请求参数到Model中。

* `@ModelAttribute` 本来的作用是绑定键值对到`Model`里，在`@ControllerAdvice`中是让全局的`@RequestMapping`都能获得在此处设置的键值对。
