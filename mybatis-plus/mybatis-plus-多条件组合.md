# mybatis-plus多条件拼装组合

> 多条件or查询

```java
//sql组合条件拼接： where a = 1 and b = 2 or (a = 2 and b = 3)
LambdaQueryWrapper<Demo> wrapper = Wrappers.lambdaQuery();
wrapper.eq(Demo::getA, 1).eq(Demo::getB, 2);
wrapper.or(queryWrapper -> {
    queryWrapper.eq(Demo::getA, 2).eq(Demo::getB, 3);
});

//select * from user where id = ? and state = ? and (age = ? or sex = ?)
LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getId, param.getId())
                .eq(User::getState, 1)
                .and(elem -> elem.eq(User::getAge, 28).or().eq(User::getSex(), "男"));
```
