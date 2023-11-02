## SqlServer存储过程

### 定义变量

```sql
-- 定义变量
DECLARE @a bigint = 1;
DECLARE @str VARCHAR(100);

SELECT @str = name FROM TABLE WHERE ID = 1
```

### 受影响行

> 若不设置SET NOCOUNT ON，可能导致@@ROWCOUNT <> 1判断失败

```sql
SET NOCOUNT ON;

UPDATE TAB SET XX=XXX WHERE ID = XXX

-- 可以通过@@ROWCOUNT获取到影像行
IF @@ROWCOUNT <> 1
```

### 语法

> 在BEGIN和END之间相当于同一作用域，可以执行访问到前面定义的变量

```sql
BEGIN
  -- 整块执行
END
```

### 游标

> 游标的作用相当于循环，能对多行数据进行遍历处理

```sql
DECLARE @value varchar(2),@name varchar(5)
DECLARE items SCROLL CURSOR FOR (SELECT 'a' AS v,'字母A' AS name UNION SELECT 'b' AS v,'字母B' AS name UNION SELECT 'c' AS v,'字母C' AS name)

OPEN items  
FETCH NEXT FROM items INTO @value, @name

WHILE @@FETCH_STATUS = 0  
BEGIN  
  PRINT @value
  PRINT @name
  FETCH NEXT FROM items INTO @value, @name
END

CLOSE items  
DEALLOCATE items
```
