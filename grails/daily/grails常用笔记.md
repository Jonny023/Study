* html转义：

不进行任何操作：
```
   <%@page defaultCodec="none" %>
   <%@ defaultCodec="none" %>
```

转义：

```
   expression为需要转义的html
    ${raw(expression)}
```

转为html:

```
   <%@page defaultCodec="HTML" %>
   <%@ defaultCodec="HTML" %>
   
 ```
 
* grails中转换json字符串

```
def json = net.minidev.json.JSONObject.toJSONString(map)
def json = new JsonBuilder(map).toPrettyString()
def json = JsonOutput.toJson(map)
//map as JSON 通过本人main方法测试好像不管用，在main里面用会报错，不知道是不是姿势不对
```

* grails用render map as JSON转换Date类型的值，在init/BootStrap.groovy的init中加入

```
//Date类型转JSON格式
JSON.registerObjectMarshaller(java.util.Date) {
    return it?.format("yyyy-MM-dd HH:mm:ss")
}
JSON.registerObjectMarshaller(Date) {
    return it?.format("yyyy-MM-dd HH:mm:ss")
}

```

* 域类绑定数据类型

```
import org.grails.databinding.BindingFormat

class DomainClass {
    String title
    @BindingFormat('MMddyyyy')  //绑定日期格式
    Date datetime
    
    @BindingFormat('UPPERCASE')  //大写
    String someUpperCaseString

    @BindingFormat('LOWERCASE') //小写
    String someLowerCaseString
}
```

* 浮点类型精度丢失，需要先转换

```
double finalScore = 2.33302
new BigDecimal(finalScore).setScale(2, BigDecimal.ROUND_HALF_UP)
```

* groovy中执行shell命令

```
class T {

    static void main(args) {
        println "ipconfig".execute().text
    }
}
```

* groovy可以实现多继承，这是java没有的

```
package com.system.aaa

class TestA {

    String hello(String name) { "Hello $name!" }
}
```

TestC类

```
package com.system.aaa

class TestC {
    String say(String msg) {
        msg
    }
}

```

TestB类，通过注解@Mixin来继承父类

```
package com.system.aaa

import org.junit.Test

@Mixin([TestA,TestC])
class TestB {

    @Test
    void test() {
        println hello("张三")
        println say("Hello,world!")
    }
}
```

* 正则表达式

> 金额验证（至多2位小数）
> 表达式：/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/

```
public static void main(String[] args) {
    Pattern pattern = Pattern.compile(/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/)
    println pattern.matcher("+10.02").matches()
    println pattern.matcher("-10.02").matches()
    println pattern.matcher("+10.0.2").matches()
    println pattern.matcher("10.02").matches()
    println pattern.matcher("10.").matches()
    println pattern.matcher("10").matches()
    println pattern.matcher("0").matches()
    println pattern.matcher("hello").matches()
}
```

* quartz禁用并发作业

```
class TestJob {

  //禁用并发
  static concurrent = false

  static triggers = {
      cron name: 'myTrigger', cronExpression: "0/5 * * * * ?"
  }

  def execute() {
      // execute job
      log.error('Start:' + new Date())
      sleep(8000)
      log.error('End:' + new Date())
  }
}
```
* SQL技巧

> 只一个sql中多次是用聚合函数count()的用法，postgreSql、mysql不用加from,但是oracle需要用from dual虚表才不会报错

```
//mysql数据库
SELECT
	(select count(colName) from tab)
	(select count(colName) from tab where 条件)

//oracle数据库
SELECT
	(select count(colName) from tab)
	(select count(colName) from tab where 条件)
   
  from dual
  
```

* grails数据绑定方式

```
//参数绑定方式
def bind = {

    //方式一
    //所有属性赋值
    def user = new UserInfo(params).save()

    //方式二
    //给所有属性赋值
    def user1 = UserInfo.get(params.id)
    user1.properties = params
    user1.save()

    //方式三
    //绑定指定对象，如/user/bind?user.username=张三&user.age=20&user.password=123456
    def user3 = new UserInfo(params['user'])
    user3.save()

    //方式四
    //给指定属性赋值
    def user4 = UserInfo.get(params.id)
    user4.properties['firstName','lastName'] = params
    user4.save()

    //方式五，使用bindData方法
    def user5 = new UserInfo()
    bindData(user5, params)  //给所有属性赋值
    bindData(user5, params, [exclude: ['username', 'password']]) //给exclude之外的属性赋值
    bindData(user5, params, [include: ["dataCreated", "sex"]])   //只给include中的属性赋值


}
```
