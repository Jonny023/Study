```sql 
# 一般只需要order by字段就行
# 如果不行就用这种方式
SELECT * FROM `user` order by INET_ATON(SUBSTRING_INDEX(CONCAT(account_name ,'.0.0.0'),'.',4))
```

