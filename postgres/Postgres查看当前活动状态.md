# Postgres查看当前活动状态

[参考地址](http://www.postgres.cn/docs/9.6/monitoring-stats.html#PG-STAT-ACTIVITY-VIEW)

[参考地址1](https://copyfuture.com/blogs-details/20210129175325774q)

> 类似mysql的`show full processlist`

### PG_STAT_ACTIVITY

```sql
SELECT * from pg_stat_activity where datname='xxx';
```

- `query_start`: 当前活动查询被开始的时间，如果`state`不是`active`，这个域为上一个查询被开始的时间
- `state`
  - `active`: 后端正在执行一个查询
  - `idle`: 后端正在等待一个新的客户端命令
  - `idle in transaction`: 后端在一个事务中，但是当前没有正在执行一个查询
  - `idle in transaction(aborted)`: 这个状态与idle in transaction相似，不过在该事务中的一个语句导致了一个错误
  - `fastpath function call`: 后端正在执行一个fast-path函数
  - `disabled`: 如果在这个后端中track_activities被禁用，则报告这个状态

>select count(*) from pg_stat_activity where state='idle'，查询闲置连接数。**注意是否忘记关闭链接**。 另外还可以查看连接数是不是过多等问题。

```sql
-- 死锁进程查看【pid为进程id】
select * from pg_stat_activity where datname='xxx' and waiting=true;

-- 慢查询
-- not()-act_start是指事务截至当前已运行时间。
-- now-query——start query截至当前已运行时间。
-- pid进程id
select datname, pid, usename, application_name, client_addr, client_port, xact_start, query_start, state_change, waiting, state, backend_xid, backend_xmin, query, xact_start, now()-xact_start from pg_stat_activity where state <> 'idle' and (backend_xid is not null or backend_xmin is not null) order by now()-xact_start;

-- 查询是否锁表
selectd oid from pg_class where relname='可能被锁的表';
select pid from pg_locks where relation='上面查询到的oid';

-- 结束进程
select pg_cancel_backend(pid);
```

