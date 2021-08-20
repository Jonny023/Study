# QueryDSL返回常量

```java
//Expressions.asString("正常").as("result") == 正常 as result，但是不会在sql中体现
//输出sql: select user0_.id as col_0_0_, user0_.username as col_1_0_, user0_.nickname as col_2_0_ from t_user user0_ where user0_.id>?
QUser user = QUser.user;
StringExpression stringExpression = Expressions.asString("正常");
JPAQuery<UserVO> query = this.jpaQueryFactory.select(
    Projections.bean(
        UserVO.class,
        user.id,
        user.username,
        user.nickname,
        stringExpression.as("result")
    )
).from(user).where(user.id.gt(0));
List<UserVO> userVOS = query.fetch();
System.out.println(userVOS);
```

