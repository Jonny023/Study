# 导出csv

```sql
# 设置编码，用默认编码就行修改后无法查询
SET client_encoding = 'ISO_8859_5';
SET client_encoding = 'UTF8';
set client_encoding to 'utf8';
\encoding GBK

# 查看服务端编码
show server_encoding;

# 查看客户端编码
show client_encoding;
\encoding

# 查看表结构
\d table_name
select column_name,data_type from information_schema.columns where table_name='b82ce9b8-2a45-406f-acfa-17c65d9853a0';

# sql命令窗口下执行，注意字段类型不匹配的需要强转
# sql查询导出csv
COPY (select * from "tableName") TO '/home/gpadmin/export.csv' WITH DELIMITER ',' CSV HEADER;
copy (select * from "tableName" where id < 10) TO '/home/gpadmin/export.csv' WITH DELIMITER ','  CSV HEADER ENCODING 'UTF8';
COPY (select * from "tableName0" where id < 10) TO '/home/gpadmin/export.csv' DELIMITER ',' csv header ENCODING 'UTF8';
# 导出到csv，null值处理为“”
copy (select * from "tableName" where cast(id as numeric) < 20) to '/home/gpadmin/export.csv' (FORMAT 'csv', DELIMITER ',', HEADER true, NULL '', ENCODING 'UTF8');
copy "tableName" to '/home/gpadmin/export.csv' (FORMAT 'csv', DELIMITER ',', HEADER true, NULL '', ENCODING 'UTF8');
```
