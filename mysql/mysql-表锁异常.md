* 查看锁

```sql
select * from performance_schema.data_locks;
```

> mysql出表锁表如何处理（com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exc）

```sql
mysql -u root -p123
use mysql;
SELECT trx_mysql_thread_id FROM INFORMATION_SCHEMA.INNODB_TRX;
kill 进程号;
```
