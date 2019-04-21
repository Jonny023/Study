# 约束

```groovy
class User {

    String login
    String password
    String firstName
    String lastName
    String email

  static constraints = {
      login(unique:true,length:5..15) // 唯一且长度为5-15位
      password(matches:/[\w\d]+/, length:6..12) // 密码为字母数字组合6至12位
      email(email:true) // 邮箱
      firstName(blank:false) // 非空字符串
      lastName(blank:false) 
  }
}
```

* 自定义约束验证

```groovy
class User {
     ...
     static constraints = {
         password(unique:true,length:5..15, validator: { val, obj ->
             if(val?.equalsIgnoreCase(obj.firstName)) {
                return false
             }
         }
     }
 }
 
 
```

## 约束属性说明

|属性|用法|说明|
|--|--|--|
|blank| login(blank:false)  |非空字符|
|email| email(email:true) |邮箱|
|inList| login(inList:["Joe", "Fred"] )| 只能为集合中的某个值 |
|length| login(length:5..15)| 字符或数组长度 |
|min| age(min:new Date())| 最小值|
|minLength| login(minLength:5)| 设置字符串的最小长度或数组属性|
|minSize| children(minSize:5)| 集合最大大小或数值的最大大小 |
|matches| login(matches:/[a-zA-Z]/)| 正则匹配 |
|max| age(max:new Date())| 最大值 |
|maxLength| login(maxLength:5)| 设置字符串的最大长度或数组属性 |
|maxSize| children(maxSize:25)|最大值|
|notEqual| login(notEqual:"Bob")|字符不等于|
|nullable |age(nullable:false)|不能为空|
|range| age(range:minAge..maxAge)|允许在范围之内|
|size| children(size:5..15) |值大小|
|unique| login(unique:true) |唯一|
|url| url(url:true) |网址|
