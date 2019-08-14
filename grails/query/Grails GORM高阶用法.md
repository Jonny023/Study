## Grails GORM高阶用法

## [官方文档](http://gorm.grails.org/latest/hibernate/manual/index.html)

* No.1 根据条件查询返回需要的字段

```groovy
def colors = Color.where {
    dateCreated == params.date
}.property("name").property("shade").property("dateCreated").list()
// 返回[['', '', '']]
```

* No.2 根据条件批量删除

```bash
Person.where {
        name == "Fred"
}.deleteAll()

def criteria = new DetachedCriteria(Person).build {
    eq 'lastName', 'Simpson'
}
int total = criteria.deleteAll()
```

* No.3 根据条件批量更新

```groovy
Person.where {
    'in'("id", ids)
}.updateAll(status: true)

def criteria = new DetachedCriteria(Person).build {
    eq 'lastName', 'Simpson'
}
int total = criteria.updateAll(lastName:"Bloggs")
```

* No.4 连表查询

```groovy
Author.where {
    name == "Stephen King"
}.join('location')
 .list()
```

* No.5 正则批匹配

```groovy
def query = Person.where {
     firstName ==~ ~/B.+/
}
```

* No.6 排序

```groovy
Pet.where {
    owner.firstName == "Fred"
}.list(sort:"owner.lastName")
```

* No.7 子查询

| 方法       | 描述                                    |
| :----------- | :--------------------------------------------- |
| **avg**      | 平均值                                         |
| **sum**      | 求和                                           |
| **max**      | 最大值                              |
| **min**      | 最小值                                         |
| **count**    | 总记录数                        |
| **property** | 指定字段 |



### 常用函数

| 方法     | 描述 |
| :--------- | :---------- |
| **second** | 秒（日期）          |
| **minute** | 分（日期）          |
| **hour**   | 小时（日期）        |
| **day**    | 天（日期）          |
| **month**  | 月（日期）          |
| **year**   | 年（日期）          |
| **lower**  | 转换为小写  |
| **upper**  | 转换为大写  |
| **length** | 计算长度    |
| **trim**   | 去除首位空字符        |

### 例

```groovy
def users = User.where {
    upper(username) == 'ADMIN'
}.list()
```



```groovy
// 查询年龄大于平均年龄的用户
final query = Person.where {
  age > avg(age)
}

// 查询年龄大于指定年龄段的平均年龄的数据
def users = User.where {
    id in where { age >= 18 }.id
}.list()

// 查询用户年龄大于平均年龄的用户
def users = User.where {
    age > where { age > 18 }.avg('age')
}.list()
// 对应sql
// select * from user this_ where this_.age > (select avg(this_.age) as y0_ from user this_ where this_.age>?)

// 查询用户年龄不等于22岁的用户
def users = User.withCriteria {
    notIn "username", User.where { age == 22 }.username
}

def users = User.where {
    def user = User
    exists User.where {
        def usr = User
        return user.id == usr.id && id == 2
    }.id()
}.list()

// 查询年龄小于张三年龄的用户
def users = User.where {
    age < property(age).of { username == 'zhangsan' }
}.list()
```

* No.7 范围查询

```groovy
def query = Person.where {
     age in 18..65
}
```

