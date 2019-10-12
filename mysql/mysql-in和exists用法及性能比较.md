# `in`和`exists`用法及性能比较

#### [参考](https://www.cnblogs.com/beijingstruggle/p/5885137.html)

* 如果查询的两个表大小相当，那么用`in`和`exists`差别不大。 
* 如果两个表中一个较小，一个是大表，则子查询表大的用`exists`，子查询表小的用`in`
* 如果使用了`not in`那么内外表都进行全表扫描，没有用到索引；而`not extsts`的子查询依然能用到表上的索引。所以无论那个表大，用`not exists`都比`not in`要快。
