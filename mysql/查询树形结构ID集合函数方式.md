## 注意

  * 写的返回长度为1000 可自定义
  * 记得替换你的表名及id pid
  
1. 查询子集

```
CREATE FUNCTION `getChildLst`(rootId INT) RETURNS varchar(1000) CHARSET gbk
    DETERMINISTIC
BEGIN
DECLARE sTemp VARCHAR(1000);
   DECLARE sTempChd VARCHAR(1000);
   SET sTemp = '';
   SET sTempChd =CAST(rootId AS CHAR);
   WHILE sTempChd IS NOT NULL DO
SET sTemp = CONCAT(sTemp,',',sTempChd);
SELECT GROUP_CONCAT(id) INTO sTempChd FROM 表名 WHERE FIND_IN_SET(pid,sTempChd)>0;
   END WHILE;
   RETURN sTemp;
END
```

2. 查询父集

```
CREATE FUNCTION `getFatherLst`(rootId INT) RETURNS varchar(1000) CHARSET gbk
    DETERMINISTIC
BEGIN 
    DECLARE sParentList VARCHAR(1000); 
    DECLARE sParentTemp VARCHAR(1000); 
SET sParentTemp =CAST(rootId AS CHAR); 
    WHILE sParentTemp IS NOT NULL DO 
        IF (sParentList IS NOT NULL) THEN 
            SET sParentList = CONCAT(sParentTemp,',',sParentList); 
        ELSE 
            SET sParentList = CONCAT(sParentTemp); 
        END IF; 
        SELECT GROUP_CONCAT(pid) INTO sParentTemp FROM 表名 WHERE FIND_IN_SET(id,sParentTemp)>0; 
    END WHILE; 
RETURN sParentList; 
END
```

## 说明

|参数|说明|
|--|--|
| rootId |	传入参数，变量（无需修改），调用时传入需查询的id|
| id |	需要查询的id |
| pid	| 需要查询记录的父id |

+ 调用的时候用`select getChildLst(20)`及`select getFatherLst(20)`
