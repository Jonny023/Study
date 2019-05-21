# Grails查询使用注意事项
* `findByXxx`这种格式不支持太多字段，比如：`findAllByUserAndAgeAndSexAndNickName()`，长度太长会报错

* 查询所有

```groovy
def allBookmarks = Bookmark.findAll('from Bookmark')
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
