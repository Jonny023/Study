## java8把list转为map

> list按指定字段分组，多个value逗号拼装

```java
List<Test1> list = Arrays.asList(
        Test1.builder().id(1).name("技术").build(),
        Test1.builder().id(1).name("Daas").build(),
        Test1.builder().id(2).name("产品").build()
);
Map<Integer, String> collect = list.stream().collect(Collectors.toMap(Test1::getId, Test1::getName, (k1, k2) -> String.format("%s,%s", k1, k2)));
```



### 多字段分组

```java
List<String> strings = Arrays.asList("apple", "apple", "orange");

//分组
Map<String, List<String>> map = strings.stream().collect(Collectors.groupingBy(Function.identity()));
System.out.println(map);//{orange=[orange], apple=[apple, apple]}

//统计元素个数
Map<String, Long> map2 = strings.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
System.out.println(map2);//{orange=1, apple=2}

//分组计数排序（降序）
Map<String, Long> finalMap = new LinkedHashMap<>();
map2.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
    .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
System.out.println(finalMap);//{apple=2, orange=1}


User u1 = new User(1L, 20, "张三");
User u2 = new User(1L, 20, "张三");
User u3 = new User(1L, 20, "李四");
User u4 = new User(2L, 18, "小明");
User u5 = new User(3L, 22, "小丽");
List<User> users = Arrays.asList(u1, u2, u3, u4, u5);
System.out.println(users);//[User{id=1, age=20, name='张三'}, User{id=1, age=20, name='张三'}, User{id=1, age=20, name='李四'}, User{id=2, age=18, name='小明'}, User{id=3, age=22, name='小丽'}]
Map<Boolean, List<User>> result = users.stream().collect(Collectors.partitioningBy(u -> u.getAge() > 18));
System.out.println(result);//{false=[User{id=2, age=18, name='小明'}], true=[User{id=1, age=20, name='张三'}, User{id=1, age=20, name='张三'}, User{id=1, age=20, name='李四'}, User{id=3, age=22, name='小丽'}]}

//多字段分组
Map<Long, Map<Integer, List<User>>> result1 = users.stream().collect(Collectors.groupingBy(User::getId, Collectors.groupingBy(User::getAge)));
System.out.println(result1);//{1={20=[User{id=1, age=20, name='张三'}, User{id=1, age=20, name='张三'}, User{id=1, age=20, name='李四'}]}, 2={18=[User{id=2, age=18, name='小明'}]}, 3={22=[User{id=3, age=22, name='小丽'}]}}
    
```

### 多字段组合分组
```java
goodsList.stream().collect(Collectors.toMap(goods -> goods.getName() + "-" + goods.getRemarks(), Functions.identity()));
//搜集为LinkedHashMap
goodsList.stream().collect(Collectors.toMap(goods -> goods.getName() + "-" + goods.getRemarks(), Functions.identity(), LinkedHashMap::new));
```
