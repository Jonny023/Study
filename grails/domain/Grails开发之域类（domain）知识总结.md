在开发过程中，好多东西可以交给域类（domain）来进行处理，这样就不需要再业务层再次进行处理了，从而简化业务。

1、如果想要domain中的属性不生成数据库字段，可以用transients进行声明

```groovy
class Demo {

    String t_title
    String t_content
    String name
    String context

    static transients = ['name','context']

}

```

//在域类中注入服务类可以用transient,这样不会持久化到数据库

```groovy
transient emailService
```
2、关于domain的触发器使用问题，如果想要在触发器中获取原始数据，可以用getPersistentValue方法获取之前的数据。

```groovy
class Demo {

    String t_title
    String t_content
    String name
    String context

    def beforeInsert() {
        getPersistentValue("name")
        getPersistentValue("context")
    }

}
```

触发器有：

```groovy
def beforeInsert() {}
def beforeUpdate() {}
def beforeDelete() {}
def afterInsert() {}
def afterUpdate() {}
def afterDelete() {}
def beforeValidate() {}
def afterValidate() {}
def onLoad() {}
```

3、在domain中使用mapping映射(使用composite联合主键必须实现序列化接口)

```groovy
static mapping = {
    //使用composite组合id是域类必须实现implements Serializable接口
    id composite:["menu","role"] 
    version false
}
```

4、设置域类数据绑定的时候空字符串自动转为null类型，在application.groovy里面配置

```groovy
// the default value for this property is true
grails.databinding.convertEmptyStringsToNull = false
```

5、域类约束所有字段允许为null，需要注意的是，用了这种方式，就不能用默认脚手架页面了，需要自己修改，建议只在第一次建表时使用，最好还是在domain 的constraints里面将需要约束的字段一一列出来，下面这种写法容易出错

```groovy
static constraints = {
   "*" nullable:true
}
```

6、获取域类有哪些属性

```groovy
def index() {
    UserInfo.constrainedProperties.each {k,v->
        println k
    }
}
```

7、在domain中创建索引

```groovy
class Book {
    static belongsTo = [user:User]
    static mapping = {
        user index:'index_book_user_id'
    }
}
```

8、domain返回json时只返回指定字段

```groovy
JSON.registerObjectMarshaller(User) { 
     def returnArray = [:] 
     returnArray['username'] = it.username    
     return returnArray 
} 
```
