# `order by `导致分页数据重复

> 原因分析： `order by `字段值相同，且出现多个相同的（不唯一）导致


> 解决方法： `order by`后面尽量用唯一标识进行区分，如：

```mysql
select * from student order by createTime desc, sex asc, id asc
```
