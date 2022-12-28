* 设置默认为空

```properties
${jdbc.url:}
```

* 类中

```java
@Value("${testWeb.host}") // 注入属性  
private String host;  
@Value("${testWeb.startpagenumber:1}") // 设置默认值  
private Integer startPageNumber;  
@Value("${testWeb.endpagenumber: #{T(java.lang.Integer).MAX_VALUE}}") // 使用SpEL设置默认值  
private Integer endPageNumber;   
```


## spring spel解析

```java
package com.example.springbootdemo;

import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DemoTests {

    /**
     * Spel表达式
     * 参考1： <a href="https://zhuanlan.zhihu.com/p/539163585">参考1</a>
     * 参考2： http://t.zoukankan.com/strongmore-p-15335622.html
     * <dependency>
     * <groupId>org.springframework</groupId>
     * <artifactId>spring-context</artifactId>
     * <version>5.2.1.RELEASE</version>
     * </dependency>
     */
    @Test
    public void run() {

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", "张三");

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("#user");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", userMap);

        Map map = (Map) expression.getValue(context);
        System.out.println(map.get("name"));
    }


    /**
     * 调用自定义函数
     *
     * @throws NoSuchMethodException
     */
    @Test
    public void exe() throws NoSuchMethodException {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        //第一种
        //context.registerFunction("call", DemoTests.class.getDeclaredMethod("call", String.class));
        //context.registerFunction("call1", DemoTests.class.getDeclaredMethod("call1", String.class));

        //第二种 获取Method对象
        Method call = ReflectionUtils.findMethod(DemoTests.class, "call", String.class);
        Method call1 = ReflectionUtils.findMethod(DemoTests.class, "call1", String.class);
        //注册放到到自定义对象中
        context.registerFunction("call", call);
        context.registerFunction("call1", call1);

        String expression = "#call(#call1('true'))";

        String msg = parser.parseExpression(expression).getValue(context, String.class);
        System.out.println(msg);
    }

    public static String call(String msg) {
        System.out.println("execute call method.");
        if (Objects.equals("true", msg)) {
            return "中国 上海";
        }
        return "";
    }

    public static String call1(String msg) {
        System.out.println("execute call1 method.");
        return msg;
    }


    /**
     * 对象防止错误
     */
    @Test
    public void call2() {
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("score", "80");
        Map<String, Map<String, String>> map = new HashMap<>();
        map.put("result", innerMap);

        ExpressionParser parser = new SpelExpressionParser();
        //放入到根对象
        EvaluationContext context = new StandardEvaluationContext(map);
        // @xxx调用bean  #xxx调用对象变量
        //对象链式调用?.防止NPE，对象可以直接调用如：#user?.name，map只能通过方法调用:#map.get('name')
        String print = parser.parseExpression("#root?.get('result')?.get('score')").getValue(context, String.class);
        System.out.println(print);
    }

}
```
