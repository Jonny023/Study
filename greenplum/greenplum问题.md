# greenplum迁移数据问题

> 意思就是执行DDL并发操作导致

[问题描述](https://stackoverflow.com/questions/62599214/relation-not-found-this-can-be-validly-caused-by-a-concurrent-delete-operation)

> Caused by: org.postgresql.util.PSQLException: ERROR: relation not found (OID 3444865) (seg1 slice1 127.0.0.1:40001 pid=3582)2022-05-09 Detail: This can be validly caused by a concurrent delete operation on this object.

[开启GPORCA优化](https://gp-docs-cn.github.io/docs/admin_guide/query/topics/query-piv-opt-enable.html)
