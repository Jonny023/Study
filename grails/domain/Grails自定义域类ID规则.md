用过grails的童鞋都知道grails域类默认建表默认id为Long型，如果我不想用默认的id，想自己定义规则来生成怎么办呢，请看示例。

NO.1 用uuid作为表id,但是可能有“-”短杠分割
```
package com.test

class Test1 {

    String id

    String title

    Test1 (String title) {
        this.title = title
    }

    static constraints = {

    }

    static mapping = {
        id generator:'uuid.hex', params:[separator:'-']
    }
}
```
如何保存呢？保存时不需要自己给id赋值
```
new Test1("test1").save(flush:true)
```
NO.2 用uuid作为表id,去掉uuid中间的短杠
```
package com.test

class Test2 {

    String id

    String title

    Test2(String title) {
        this.title = title
    }

    static mapping = {
        id generator: "uuid.hex", type:"string"
    }

}
```
保存同NO.1

NO.3 用自己定义id，保存时自己给id赋值，assigned表示自定义
```
package com.test

class Test3 {

    String id
    String title

    Test3(String id,String title) {
        this.id = id
        this.title = title
    }

    static constraints = {

    }

    static mapping = {
        id generator:'assigned', type:'string'
    }
}
```
保存的时候需要给id赋值
```
new Test3(id,"test3").save(flush:true)
```
如何配置全局id为uuid?

在grails-app/conf下面新建一个application.groovy，里面写入：
```
grails.gorm.default.mapping = {
    id generator:'uuid.hex'
}
```
定义域类里面加上id
```
package com.system
/**
 *  角色
 */
class ShiroRole {
    String id
    //角色标识
    String name
    String roleName
    ShiroRole(String name,String roleName) {
        this.name = name
        this.roleName = roleName
    }
    static constraints = {
        name(nullable: false, blank: false, unique: true)
    }
}
```
