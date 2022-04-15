# clickhouse实用记录

[参考](https://www.bookstack.cn/read/clickhouse-21.2-zh/50e08fb3bfbf45d0.md)

### 字符反转

```sql
-- 字符串反转
-- cba
SELECT reverseUTF8('abc')
```



### 获取json值

```sql
-- 查询json字符种user下面的age的值
SELECT JSONExtractString(content_package,'user', 'age') FROM tableName;
```



### 字符拼接

```sql
-- 格式化
select format('{0},{1},{2}', a, b, c) from tab;
select format('{},{},{}', a, b, c) from tab;
select concat(a,',', b, ',', c) from tab limit 3;

-- 同concat，优化group by
select concatAssumeInjective(a,',', b, ',', c) from tab limit 3;
```



### 字符截取

```sql
select substring('hello', 1, 2); -- he，offset必须从0开始
select substr('hello', 1, 2); -- he
select mid('hello', 1, 2); -- he
```



### 字符结尾

> 若字符末尾没有o，则追加到字符末尾，否则不添加

```
select appendTrailingCharIfAbsent('hello', 'o')
```



### base64编码

```sql
select base64Encode('{"alg": "sha256"}')
-- eyJhbGciOiAic2hhMjU2In0=
```



### base64解码

> 失败抛出异常

```sql
-- 失败抛出异常
select base64Decode('eyJhbGciOiAic2hhMjU2In0=')
-- {"alg": "sha256"}

-- 失败返回空
select tryBase64Decode('eyJhbGciOiAic2hhMjU2In0=')
```



### 判断前后缀

```sql
-- 匹配后缀，区分大小写，匹配成功返回1，失败0
select endsWith('Java Web', 'b') -- 1

-- 匹配前缀
select startsWith('Java Web', 'J') -- 1
```



### 删除空白字符

```sql
-- 删除左侧空白字符
select trimLeft('      Java Web')

-- 删除右侧空白字符
select trimRight('      Java Web     ')

-- 删除两侧空白字符
select trimBoth('      Java Web     ')
```



### 大海捞针【like】

* multiSearchAllPositionsCaseInsensitiveUTF8
* multiSearchAllPositionsCaseInsensitive
* multiSearchAllPositionsUTF8

[参考](https://www.bookstack.cn/read/clickhouse-21.2-zh/50e08fb3bfbf45d0.md)

```sql
-- 不区分大小，0未找到，>0表示匹配上了
select positionCaseInsensitive('中国长城A', 'a'); -- 13

select multiSearchAllPositionsCaseInsensitive('a b c', ['a', 'c']) -- [1,5]

select multiSearchAllPositionsCaseInsensitive('a b c', ['a', 'd']) -- [1,0]

select length(multiSearchAllPositionsCaseInsensitive('a b c', ['a', 'c'])) -- 2

-- 字符转数组
select toTypeName(splitByChar(',', 'a,b,c'))
-- 匹配多个字符 [1,10,13]
select multiSearchAllPositionsCaseInsensitiveUTF8('中国互联网产业，在国内的占比', splitByChar(',', '中国,国内,占比'))
-- multiSearchAllPositionsCaseInsensitiveUTF8未匹配为0，加上has(arr, element)未匹配为0，匹配为1
select has(multiSearchAllPositionsCaseInsensitiveUTF8('中国互联网产业，在国内的占比', splitByChar(',', '中国,国内,占比,0')), 0) = 0
```



### 数组交集

```sql
-- 有交集返回1，无交集返回0
select hasAny([1,2,3,4,6], [1,3,6])
```



### 合并数组

```sql
-- [1,3,4,2,5]
select arrayConcat([1,3,4],[2,5])
```



### 数组排序

```sql
-- 降序
select arrayReverseSort(arrayConcat([1,3,4],[2,5]))
-- 升序
select arraySort(arrayConcat([1,3,4],[2,5]))
```



### 日期

[参考](https://www.bookstack.cn/read/clickhouse-21.2-zh/8e44834d51ea889b.md)

```sql
-- 星期几 1-7
select toDayOfWeek(yesterday())

-- 当月的号数
select toDayOfMonth(now())

select today() -- 2022-04-15
select yesterday() -- 2022-04-14

select toYYYYMMDDhhmmss(now()) --UInt64类型的数值
```

### 日期差

> dateDiff('unit', startdate, enddate, [timezone])

* 支持单位：second, minute, hour, day, week, month, quarter, year

```sql
select dateDiff('day', yesterday(), today()) -- 1
```

### 日期格式化

[参考](https://www.bookstack.cn/read/clickhouse-21.2-zh/8e44834d51ea889b.md)

```sql
-- 2022-04-15 10:04:55
select formatDateTime(now(), '%Y-%m-%d %H:%m:%S')

--字符转日期
select toDate(formatDateTime(now(), '%Y-%m-%d %H:%m:%S'))
select toDateTime(formatDateTime(now(), '%Y-%m-%d %H:%m:%S'))
```

### case when then else end

* multiIf(cond_1, then_1, cond_2, then_2...else)

```sql
select multiIf(1>0, '大于', '小于') -- 大于
select multiIf(1<0, '小于', 4<5, '小于', '大于') -- 小于
```
