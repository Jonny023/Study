> for example 

 ```sql
 SELECT
	* 
FROM
	jo_user 
WHERE
	concat( ifnull( `email`, '' ), `username` ) LIKE "%123456@qq.com%"
 ```
 
 * concat(str1,str2,...) 参数只能是字符，若为null，则返回结果也为null
