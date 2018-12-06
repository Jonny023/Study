> 域类字段为clob类型
```groovy
class Test {
    Clob newsContent
    
    static mapping = {
        newsContent sqlType: "text" //仅适用于mysql数据库，若是oracle数据库，字需用"CLOB"
    }
}
```

> 通过createCriteria()来实现

```groovy
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

### `newsContent sqlType: "text" `仅适用于mysql数据库，若是oracle数据库，则需用"CLOB"
