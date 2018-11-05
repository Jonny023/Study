* 通过如下sql查看条件扫描情况

```sql
explain SELECT * FROM `jc_department` where id not in (1,24,25);
```

| id | select_type | table | partitions | type | possible_keys | key | ken_len | ref | rows | filtered | Extra |
|--|--|--|--|--|--|--|--|--|--|--|--|
| 1 |	SIMPLE | jc_department | null | ALL | PRIMARY | null | null | null | 12 | 100.00 |Using where|
| 主键 | 简单/复杂查询 | 表名 | 分区 | 访问类型 |用到索引 | 索引 | 索引 | 字节数 | 匹配条件 | 扫描行数 | 满足查询的记录数量 | 其他 |
