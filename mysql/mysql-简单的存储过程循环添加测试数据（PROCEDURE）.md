> 通过循环给test表添加数据

```sql
CREATE PROCEDURE test ( ) BEGIN
DECLARE
	pid INT;

SET pid = 1000000;
WHILE
	pid > 0 DO
INSERT INTO `test`.`test` ( `id`, `name`, `age` )
VALUES
	( 1, NULL, 20 );

SET pid = pid - 1;

END WHILE;
```
