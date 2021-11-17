# sql不等于

> sql不等于（`!=`或`<>`）会把为null的数据过滤掉

```sql
-- 这个sql不会查询出为null的数据，empty string还是会查询出来
SELECT * FROM demo WHERE find_in_set("A", `level`) AND id_card <> '500243199808210135'

-- 若想要一并查出为null的数据
SELECT * FROM demo WHERE find_in_set("A", `level`) AND id_card <> '500243199808210135' or id_card is null
```

