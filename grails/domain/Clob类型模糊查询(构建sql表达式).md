> 通过createCriteria()来实现
```
Test.createCriteria().list {
    if (params.name){
        ilike("newsName","%"+params.name+"%")
    }
    if (params.newsContent){
        projections {
            //sqlRestriction  "char_length(id) >= 2"
            //sqlRestriction "news_name like ?",["%今日%"]

            //查询clob类型
            sqlRestriction "news_content like ?",["%"+params.newsContent+"%"]
        }
    }
}
```
