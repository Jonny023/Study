# Oracle

## 时间相关

```sql
select
--oracle求当年的第几周,从当年的第一个星期一算当年第一周的第一天 
to_char(CURRENT_TIMESTAMP , 'yyyyiw') as week, 
--oracle求当年的第几周,从当年的1月1日算一年的第一周的第一天
to_char(CURRENT_DATE, 'yyyyww') as week2,
--oracle求当月的第几周,从当月的1号算一周的第一天 
to_char(CURRENT_DATE, 'yyyyw') as week3,
--oracle求第几年
to_char(CURRENT_DATE, 'yyyy') as year, 
--oracle求当年的第几月
to_char(CURRENT_DATE, 'mm') as month, 
--oracle求当年的第几天
to_char(CURRENT_DATE, 'ddd') as day, 
--oracle求当年的第几季度
to_char(SYSDATE, 'q') as quarter,
--年月日十分秒
to_char(CURRENT_DATE, 'yyyy-mm-dd hh24:mi:ss') AS date_time
from dual;

-- 星期
-- 2
select to_char(sysdate - 1,'d') AS dw from dual;
-- 星期二
select to_char(sysdate,'day') day from dual;
-- tuesday
select to_char(sysdate,'day','NLS_DATE_LANGUAGE=AMERICAN') day from dual;


select
--oracle求当年的第几周,从当年的第一个星期一算当年第一周的第一天 
to_char(TO_DATE('20211225', 'YYYYMMDD') , 'yyyyiw') as week, 
--oracle求当年的第几周,从当年的1月1日算一年的第一周的第一天
to_char(TO_DATE('20211225', 'YYYYMMDD'), 'yyyyww') as week2
FROM dual
```

