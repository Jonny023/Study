## createSQLQuery方法映射到具体的VO类

```java
list = session.createSQLQuery("SELECT COUNT(id) as value,type as label FROM CARS GROUP BY type ")
    .addScalar("value", IntegerType.INSTANCE)
    .addScalar("label", StringType.INSTANCE)
    .setResultTransformer(new AliasToBeanResultTransformer(ChartsLabelVO.class)).list();
    
    
    
    
    
    
public List<MessageExtDto> getMessagesForProfile2(Long userProfileId) {
    Query query = getSession().createSQLQuery("  "
            + " select a.*, b.* "
            + " from messageVO AS a "
            + " INNER JOIN ( SELECT max(id) AS id, count(*) AS count FROM messageVO GROUP BY messageConversation_id) as b ON a.id = b.id "
            + " where a.id > 0 "
            + " ")
            .addScalar("id", new LongType())
            .addScalar("message", new StringType())
            ......... your mappings
            .setResultTransformer(Transformers.aliasToBean(MessageExtDto.class));

    List<MessageExtDto> list = query.list();
    return list
}
    
    
String sql = "select t_student.* from t_student";
//String sql = "select t_student.id,t_student.name,... from t_student"; //select all column
SQLQuery query = session.createSQLQuery(sql);
query.addEntity(Student.class);//or query.addEntity("alias", Student.class);
//query.list();[Student@..., Student@..., Student@...]
query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP); //or other transformer
query.list(); //[{Student(or alias)=Student@...},{Student=Student@...}]    
```

* [查看跟更多](https://docs.jboss.org/hibernate/core/3.6/reference/en-US/html_single/)
