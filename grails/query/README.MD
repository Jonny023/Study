# Grails查询使用注意事项
* `findByXxx`这种格式不支持太多字段，比如：`findAllByUserAndAgeAndSexAndNickName()`，长度太长会报错

* sql按照日期排序，且空值最后排序
```groovy
def list= User.createCriteria().list(params, {
    order(org.hibernate.criterion.Order.asc('createTime').nulls(org.hibernate.NullPrecedence.LAST))
    order("updateTime", "desc")
})

def userCriteria = User.createCriteria()
List results = userCriteria.list(max:limit, offset:offset) {
    eq("isActive", true)
    ilike("firstName",text+"%")
    userCriteria.addOrder(Order.asc("firstName").nulls(NullPrecedence.LAST));
}

or{
    userCriteria.addOrder(Order.asc(USERNAME).nulls(NullPrecedence.LAST));
    userCriteria.addOrder(Order.asc(EMAIL).nulls(NullPrecedence.LAST));
    userCriteria.addOrder(Order.asc(FIRST_NAME).nulls(NullPrecedence.LAST));
    userCriteria.addOrder(Order.asc(LAST_NAME).nulls(NullPrecedence.LAST));
}
```

* `hql`空值排到最后

```groovy
// oracle
User.executeQuery("from User u order by createTime desc nulls last")

// mysql
//order by -createTime desc  //加上-，将降序的倒转过来就是升序排序，null值的最后了使用降序排序，但null排最前
// order by -createTime asc //同理，将升序倒转过来就是降序排序，null值最前
User.executeQuery("from User u order by -createTime desc")
```

* 查询所有

```groovy
def allBookmarks = Bookmark.findAll('from Bookmark')
```

* `Criteria`常用表达式

|操作符|Criteria方法|描述|
|--|--|--|
|==|eq|等于|
|!=|ne|不等于|
|>|gt|大于|
|<|lt|小于|
|>=|ge|大于等于|
|<=|le|小于等于|
|in|inList|包含|
|==~|like|区分大小写模糊匹配|
|=~|ilike|不区分大小写模糊匹配|

* 排序

```groovy
// 多字段排序
def users = User.findAllByUsername('张三', [sort: ['dateCreated':'desc', 'xx': 'asc']])

// 单字段排序
def users = User.list([sort: 'dateCreated', order: 'desc'])

def users = User.createCriteria().list {
    order("dateCreated", "desc")
    order("xxx", "asc")
}
```


* 查询标题等于Grails的单个对象

```groovy
def grailsBookmark = Bookmark.find('from Bookmark as b where b.title = ?',['Grails'])
```

* 查询最新的10条记录

```groovy
def lastTen = Bookmark.list(offset:Bookmark.count()-10, sort:'dateCreated', order:'desc')
```

* 抓取策略

> 查询的时候多表自动join

```groovy
def bookmarks = Bookmark.findAllByTitle("Grails",[fetch:[tags:"eager"]])
```

* 模糊查询

```groovy
def bookmark = Bookmark.findByTitleLike('Grails%')
```

* 日期范围查询

```groovy
def today = new Date()
def last10Days = Bookmark.findByDateCreatedBetween(today-10,today)
```

* 不匹配查询

```groovy
def somethingElse = Bookmark.findByTitleNotEqual('Grails')
```

## 常用方法

|说明|方法|参数个数|用法|
|--|--|--|--|
|两者之间|Between| 2| Bookmark.findByDateCreatedBetween(today-10,today)|
|等于字符|Equals| 1| Bookmark.findByTitleEquals('Grails')|
|大于|GreaterThan| 1| Bookmark.findByDateCreatedGreaterThan(lastMonth)|
|大于等于|GreaterThanOrEqual| 1| Bookmark.findByDateCreatedGreaterThanOrEqual(lastMonth)|
|等于空|IsNull| 0| Bookmark.findByNotesIsNull()|
|不等于空|IsNotNull| 0| Bookmark.findByNotesIsNotNull()|
|小于|LessThan| 1| Bookmark.findByDateCreatedLessThan(lastMonth)|
|小于等于|LessThanOrEqual| 1| Bookmark.findByDateCreatedLessThanOrEqual(lastMonth)|
|模糊|Like| 1| Bookmark.findByTitleLike('Grails%')|
|不等于字符|NotEqual| 1| Bookmark.findByTitleNotEqual('Grails')|

## 通过`createCriteria()`构建查询

```groovy
def c = Bookmark.createCriteria()
def results = c.list {
 like('title', 'Grails%')
 between('dateCreated', new Date()-10, new Date())
}
```

* 只返回指定列，下例只返回`url`
```groovy
def criteria = Bookmark.createCriteria()
def results = criteria.list {
   projections {
      countDistinct('url')
   }
   tags {
      ilike('name', 'Grails')
   }
}
```

* 抓取策略

```groovy 
import org.hibernate.FetchMode as FM

def criteria = Bookmark.createCritera()
def bookmarks = criteria.list {
  eq("title","Grails")
  fetchMode("tags", FM.EAGER)
}
```

* 映射返回数据类型

```groovy
def user = UserInfo.createCriteria().get {
    // 映射返回数据类型为map键值对
    resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
    eq("username", "admin")
    projections {
        // property第一个参数为domain属性，第二个参数为返回键值对的键名，类似数据库as username
        property("username", "username")
    }
}


class AccountsOverview {
    Number allBalances
    Number lastNames
}

def c = Account.createCriteria()

def accountsOverview = c.get {
    resultTransformer(Transformers.aliasToBean(AccountsOverview))
    projections {
        sum('balance', 'allBalances')
        countDistinct('holderLastName', 'lastNames')
    }
}
```

* 正则查询

```groovy
def payBase = PayBasic.withCriteria {
    projections {
        max("payOrderNo")
    }
    rlike("payOrderNo",/^${creditCard}/)
}
```

* 查询关联对象的属性

```groovy
def users = User.createCriteria().list {
    role {
        projections {
            property("id")
        }
    }
    addr {
        eq("id", (params.id?:'0') as long)
    }
}
```

* 子查询

```groovy
def subQuery = Page.where({
   components {
      eqProperty("id", "this.id") //this: is the root alias which refers to the Component
   }
   setAlias("page")
})


Component.createCriteria().list {
  exists(subQuery.id())
}
```

* `sqlProjection`直接查询数据库字段

```groovy
def messages = InfoTalent.createCriteria().list {
    resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
    projections {
        property("id", "id")
        property("infoTheme", "infoTheme")
        property("infoCreateTime", "infoCreateTime")
        sqlProjection "CASE WHEN INFO_STATE = 0 THEN '未读' ELSE '已读' END as infoState", 'infoState', org.hibernate.type.StringType.INSTANCE
        // property("infoState", "infoState")
    }
    unitBasic {
        eq("userInfo", user)
    }
}
```

* `Oracle`数据库`in`查询限制长度为1000

```groovy
def result = MyDomain.createCriteria().list {
    or { idList.collate(1000).each { 'in'('id', it) } }
}
```



## [参考一](http://gorm.grails.org/6.1.x/hibernate/manual/index.html)
## [参考二](https://gorm.grails.org/latest/hibernate/manual/)
