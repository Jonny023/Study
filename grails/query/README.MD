# Grails查询使用注意事项
* `findByXxx`这种格式不支持太多字段，比如：`findAllByUserAndAgeAndSexAndNickName()`，长度太长会报错

* 查询最新的10条记录

```groovy
def lastTen = Bookmark.list(offset:Bookmark.count()-10, sort:'dateCreated', order:'desc')
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
|等于|Equals| 1| Bookmark.findByTitleEquals('Grails')|
|大于|GreaterThan| 1| Bookmark.findByDateCreatedGreaterThan(lastMonth)|
|大于等于|GreaterThanOrEqual| 1| Bookmark.findByDateCreatedGreaterThanOrEqual(lastMonth)|
|等于空|IsNull| 0| Bookmark.findByNotesIsNull()|
|不等于空|IsNotNull| 0| Bookmark.findByNotesIsNotNull()|
|小于|LessThan| 1| Bookmark.findByDateCreatedLessThan(lastMonth)|
|小于等于|LessThanOrEqual| 1| Bookmark.findByDateCreatedLessThanOrEqual(lastMonth)|
|模糊|Like| 1| Bookmark.findByTitleLike('Grails%')|
|不等于|NotEqual| 1| Bookmark.findByTitleNotEqual('Grails')|
