# 自定义方言

> 如何实现查询时按中英文排序？

* 目前有两种方案
    * 通过`mysql`中`convert`函数
    * 通过自定义方言引入`mysql`的`convert`函数
    

## 方案一

* 直接通过`hibernate`的`createSQLQuery`方法实现

```groovy
/**
 *  纯SQL查询并按昵称升序
 *  英文最先排序，然后是中文
 */
def list = {
    def lists = User.withSession { Session session ->
        session.createSQLQuery("select * from user u order by convert(u.nickname using gbk) ").addEntity(User)
    }.list()
    render lists as JSON
}

/**
 *  SQL查询并按昵称升序
 *  英文最先排序，然后是中文
 *  collate gbk_chinese_ci 忽略大小写
 */
def enAndZh = {
    def lists = User.withSession { Session session ->
        session.createSQLQuery("select * from user u order by convert(u.nickname using gbk) collate gbk_chinese_ci").addEntity(User)
    }.list()
    render lists as JSON
}
```

## 方案二

* 继承`org.hibernate.dialect.MySQLDialect`类

```groovy
package org.hibernate.dialect.custom;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 *  自定义方言，添加mysql函数
 * @Author Lee
 * @Description
 * @Date 2019年03月20日 14:51
 */
public class MySQLExtendDialect extends MySQLDialect {

    public MySQLExtendDialect() {
        super();
        registerFunction("convert_gbk",new SQLFunctionTemplate(StandardBasicTypes.STRING, "convert(?1 using gbk)") );
    }
}
```

* 配置`application.yml`中`hibernate`方言为自定义的方言类

```yaml
hibernate:
    cache:
        queries: false
        use_second_level_cache: false
        use_query_cache: false
    dialect: org.hibernate.dialect.custom.MySQLExtendDialect
```

* 查询`HQL`中使用自定义函数

```groovy
/**
 *  自定义方言实现HQL昵称排序
 *  英文最先排序，然后是中文
 */
def sort = {
    def list = User.executeQuery("from User u order by convert_gbk(u.nickname) asc")
    render list as JSON
}
```
