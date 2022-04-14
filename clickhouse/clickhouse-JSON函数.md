* [JSON函数](https://www.bookstack.cn/read/clickhouse-21.2-zh/7aae2ba6e433a6fd.md)

```sql
-- 查询json字符种user下面的age的值
SELECT JSONExtractString(content_package,'user', 'age') FROM tableName;
```
