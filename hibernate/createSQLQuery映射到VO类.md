## createSQLQuery方法映射到具体的VO类

```java
list = session.createSQLQuery("SELECT COUNT(id) as value,type as label FROM CARS GROUP BY type ")
    .addScalar("value", IntegerType.INSTANCE)
    .addScalar("label", StringType.INSTANCE)
    .setResultTransformer(new AliasToBeanResultTransformer(ChartsLabelVO.class)).list();
```
