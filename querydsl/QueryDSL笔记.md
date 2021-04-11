# QueryDSL笔记

* 映射投影自定义实体类，映射属性名称一样就不用别名`as()`

```java
DayFlowVO dayFlow = this.jpaQueryFactory.select(
    Projections.bean(
        DayFlowVO.class,
        queryDay.pv.castToNum(Long.class).sum().as("pv"),
        queryDay.uv.castToNum(Long.class).sum().as("uv"),
        queryDay.ipCount.castToNum(Long.class).sum().as("ip")
    )
).from(queryDay).where(queryDay.si.eq(ServletUtils.getAppKey()).and(queryDay.time.between(dateByDay.getStart(), dateByDay.getEnd()))).fetchOne();
```

* 联表查询

```java
List<cn.com.geely.dao.jpa.entity.Resource> appResources = jpaQueryFactory.select(resource)
    .from(accessControlList)
    .leftJoin(resource)
    .on(accessControlList.resourceId.eq(resource.id))     .where(accessControlList.subjectType.eq(Const.APP_RESOURCE).and(accessControlList.subjectId.eq(appId))).fetch();
```

* `sql`自定义函数

  ```java
  QAdsBuriedPointWeb5S entity = QAdsBuriedPointWeb5S.adsBuriedPointWeb5S;
  //StringTemplate stringTemplate = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", entity.time, "%Y-%m-%d");
  StringTemplate stringTemplate = Expressions.stringTemplate("to_char(to_timestamp({0}/1000), {1})", entity.time, "YYYY-MM-DD HH24:MI:SS");
  List<AdsBuriedPointWeb5S> fetch = jpaQueryFactory.selectFrom(entity).where(stringTemplate.like("2021-03-%")).fetch();
  ```

* `Tuple`数据处理

```java
List<Tuple> lists = this.jpaQueryFactory
                .select(daybeforeTrackevent.amount.sum(), daybeforeTrackevent.time)
                .from(daybeforeTrackevent)
                .where(daybeforeTrackevent.time.between(dateListRange.getStart().getTime(), dateListRange.getEnd().getTime())
                        .and(daybeforeTrackevent.action.eq(form.getAction()))
                        .and(daybeforeTrackevent.si.eq(ServletUtils.getAppKey())))
                .groupBy(daybeforeTrackevent.time)
                .fetch();

        dateListRanges.stream().map(JodaTimeUtil.DateListRange::getStart).forEach(date-> {
            TrackeventVO trackeventVO = new TrackeventVO();
            Optional<Tuple> optional = lists.stream().filter(data -> data.get(1, Long.class).equals(date.getTime())).findFirst();
            if (optional.isPresent()) {
                trackeventVO.setCount(optional.get().get(0, Integer.class));
            } else {
                trackeventVO.setCount(0);
            }
            trackeventVO.setTime(date);
            arrayList.add(trackeventVO);
        });
```

* `case when`

```java
QUser user = QUser.user;
QPost post = QPost.post;
NumberExpression<Integer> java = post.category.name.lower().when("java").then(1).otherwise(0);
NumberExpression<Integer> python = post.category.name.lower().when("python").then(1).otherwise(0);
return queryFactory.selectFrom(user)
    .leftJoin(user.posts, post)
    .select(user.name, user.id, java.sum(), python.sum(), post.count())
    .groupBy(user.id)
    .orderBy(user.name.desc())
    .fetch()
    .stream()
    .map(tuple -> {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("username", tuple.get(user.name));
        map.put("java_count", tuple.get(java.sum()));
        map.put("python_count", tuple.get(python.sum()));
        map.put("total_count", tuple.get(post.count()));
        return map;
    }).collect(Collectors.toList());
```

* 组合条件

```java
QSysUser userModel = QSysUser.sysUser;
BooleanBuilder builder = new BooleanBuilder();
BooleanBuilder orBuilder = new BooleanBuilder();
builder.and(userModel.logicDelete.eq(Boolean.FALSE));
orBuilder.or(userModel.username.eq(username));
orBuilder.or(userModel.phone.eq(username));
orBuilder.or(userModel.email.eq(username));
SysUser user = (SysUser) super.jpaQueryFactory
    .select(userModel).from(userModel)
    .where(builder.and(orBuilder))
    .fetchOne();
```

* `sql`函数

```java
@Autowired
private JPAQueryFactory jpaQueryFactory;

StringTemplate stringTemplate = Expressions.stringTemplate("to_char(to_timestamp({0}/1000), {1})", entity.time, "YYYY-MM-DD HH24:MI:SS");
        List<AdsBuriedPointWeb5S> fetch = jpaQueryFactory.selectFrom(entity).where(stringTemplate.like("2021-03-%")).fetch();


StringTemplate stringTemplate = Expressions.stringTemplate("to_char(to_timestamp({0}/1000), {1})", entity.time, ConstantImpl.create("YYYY-MM-DD HH24:MI:SS"));
        DateTemplate<Timestamp> time = Expressions.dateTemplate(Timestamp.class, "to_timestamp({0}/1000)", entity.time, ConstantImpl.create("YYYY-MM-DD"));
        List<Tuple> list = jpaQueryFactory
                .select(entity.pv.sum(), time)
                .from(entity)
                .where(stringTemplate.like("2021-03-%"))
                .groupBy(time)
                .fetch();
```

