> 在`properties`或`yaml`配置文件中是可以使用占位符的

### 1、随机数

* `${random.value}` - 类似`uuid`的随机数，没有"`-`"连接
* `${random.int}` - 随机取整型范围内的一个值
* `${random.long}` - 随机取长整型范围内的一个值
* `${random.long(100,200)}` - 随机生成长整型`100-200`范围内的一个值
* `${random.uuid}` - 生成一个`uuid`，有短杠连接
* `${random.int(10)}` - 随机生成一个10以内的数
* `${random.int(100,200)}` - 随机生成一个`100-200` 范围以内的数

> 例子

```properties
#user.username=${random.value}
#user.username=${random.int}
#user.username=${random.long}
#user.username=${random.uuid}
#user.username=${random.int(10)}
#user.username=${random.int(100,200)}
user.username=${random.long(100,200)}
```



### 2、占位符

* `${key:defaultValue}` - 若`key` 不存在，则将`defaultValue`的值赋值给取值的对象

> 例子

* `${user.username}` 当在配置文件中用这个表达式，而user.username未定义的时候，取值时会将`${user.username}` 当做字符串处理

```properties
user.username=${test.xx:周伯通}
```



