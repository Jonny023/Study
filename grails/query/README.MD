# Grails查询使用注意事项
* `findByXxx`这种格式不支持太多字段，比如：`findAllByUserAndAgeAndSexAndNickName()`，长度太长会报错

* 查询最新的10条记录

```groovy
def lastTen = Bookmark.list(offset:Bookmark.count()-10, sort:'dateCreated', order:'desc')
```
