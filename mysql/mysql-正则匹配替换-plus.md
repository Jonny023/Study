* 正则匹配

```sql
select * from jc_file where file_path REGEXP '^(/jeecmsv9f/)'
```

* 替换掉jc_file表中file_path字段以/u/开头的值为/test/u/

```sql
update `jc_file` set file_path = replace(file_path,"/u/","/test/u/") where file_path REGEXP '^(/u/)'
```
