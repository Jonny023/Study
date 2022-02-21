# PostgreSQL精度问题

### 1.字符转小数数值

```sql
select coalesce(nullif(total, ''), '0')::decimal from kpi
```

