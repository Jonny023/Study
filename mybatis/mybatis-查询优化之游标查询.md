## 查询优化之游标查询

* 游标查询不同于cursor，cursor是一条一条的读，效率并不高，适用于数据同步之类的，对效率要求不高的业务场景
* 游标是多次读，每次读取多条，像分页一样，直到所有数据读取完成

[参考地址](https://mp.weixin.qq.com/s/wOhoRAJ78EciETYcqa0pKQ)

### 说明

```java
// 方式一 多次获取，一次多行
@Select("SELECT bds.* FROM big_data_search bds ${ew.customSqlSegment} ")
@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000000)

// 方式二 一次获取，一次一行
@Select("SELECT bds.* FROM big_data_search bds ${ew.customSqlSegment} ")
@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 100000)
@ResultType(BigDataSearchEntity.class)
```

### mapper
```java
@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 200)
List<DemoDTO> query(@Param("param") DemoRequest params);
```

### Service业务处理

> 采用多线程处理(前提是需要对查询结果进一步处理)

```java
public List<DemoDTO> notInGoods(DemoRequest params) {
    List<DemoDTO> lists = this.baseMapper.notInGoods(params);
    CountDownLatch downLatch = ThreadUtil.newCountDownLatch(lists.size());
    lists.forEach(item -> {
        try {
            this.getExecutorService().submit(() -> buildGoodsData(item));
        } finally {
            downLatch.countDown();
        }
    });
    return lists;
}
```
