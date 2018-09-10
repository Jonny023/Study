1、mysql通过replace函数实现字符替换
```
update `user` SET `name` = replace (`name`,'张','赵') where `name` LIKE '%张%'
```
2、find_in_set函数使用
```
SELECT * FROM `user` where FIND_IN_SET(age,"18,20,22");
```
3、查看mysql版本
```
select version();
```
4、查看数据库下所有表创建日期
```
select * from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = 'activiti' order by create_time desc;
SELECT table_schema,table_name,create_time FROM information_schema.tables WHERE table_schema='activiti'
```
5、查看数据库某一个表创建日期
```
select CREATE_TIME from INFORMATION_SCHEMA.TABLES where TABLE_NAME='protocols' 
```
