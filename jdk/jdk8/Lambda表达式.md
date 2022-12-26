## forEach()方法

> 集合（List）

```java
// 无条件判断
List list = Arrays.asList("语文","数学","英语");
list.forEach(obj-> System.out.println(obj));

// 条件判断
list.forEach(obj-> {
    if("数学".equals(obj)) {
        System.out.println("今天有数学课。");
    }
});

// 直接打印结果
list.forEach(System.out::println);
```

> 键值对（Map）

```java
Map<String, Object> map = new HashMap<String, Object>();
map.put("name","张三");
map.put("age",20);
map.put("sex",'男');
map.forEach((k, v) -> System.out.println("键：" + k + "，值：" + v));

System.out.println("");


map.forEach((k, v) -> {
    System.out.println("键：" + k + "，值：" + v);
});
```

> 数组（Array）

```java
String[] words = new String[]{"lianggzone", "spring", "summer", "autumn", "winter"};
Arrays.sort(words, (x, y) -> x.compareToIgnoreCase(y));
```

## stream()方法
* filter() 根据条件过滤

> 基本用法
```java
List list = Arrays.asList("语文", "数学", "英语");
long count = list.stream().filter(o -> o.toString().indexOf("语") != -1).count();

// 输出2
System.out.println(count);

```

> 条件过滤

```java
List list = Arrays.asList("语文", "数学", "英语");
Stream stream = list.stream().filter(o -> o.toString().indexOf("语") != -1);

// 输出 语文 英语
stream.forEach(o-> System.out.println(o));

List<String> list1 = Arrays.asList("Java", "Python", "Php");
List<String> list2 = Arrays.asList("Dubbo", "Zookeeper");
// 将多个list合并为一个list
List<String> list3 = Stream.of(list1, list2).flatMap(l-> l.stream()).collect(Collectors.toList());
System.out.println(list3 );

List<List<String>> list4 = Arrays.asList(Arrays.asList("100", "20"), Arrays.asList("39", "40"));
// 将List<String>合并为一个list，并转换为Integer类型【将List<String>】转为List<Integer>
IntStream intStream = list4.stream().flatMapToInt(child-> child.stream().mapToInt(Integer::new));
List<Integer> ints = intStream.boxed().collect(Collectors.toList());
System.out.println(ints);

int[] arrays = {100, 101, 88, 92, 44};
// 数组转换为集合
List<Integer> arr2List = Arrays.stream(arrays).boxed().collect(Collectors.toList());
arr2List.stream().forEach(System.out::println);

List<User> users = Arrays.asList(
	new User(1L, '男', "admin"),
	new User(2L, '男', "test"),
	new User(3L, '男', "guest")
);

// 获取所有用户id集合
List<Long> ids = users.stream().map(User::getId).collect(Collectors.toList());
System.out.println(ids);

// 获取用户指定属性封装为Map
Map<Long, String> map = users.stream().collect(Collectors.toMap(User::getId, User::getUsername));

// 键重复，按键去重
Map<String, SysTarget> targetMap = targets.stream().collect(Collectors.toMap(SysTarget::getTargetValue, o -> o, (k, v) -> k));

// 将指定属性封装为VO类
List<UserVO> userVOList = users.stream().map(user -> {
	return new UserVO(user.getId(), user.getUsername());
}).collect(Collectors.toList());
System.out.println(userVOList);




List<Integer> list = Arrays.asList(1, 2, 3, 1, 2, 4, 5, 20);
        //统计list每个元素出现的次数
        Map<Integer, Integer> map = list.stream() // list 对应的 Stream
                .collect(Collectors.toMap(e -> e, e -> 1, (a, b) -> a + b));
        System.out.println(map);


//存在重复的元素集合
List<Integer> result = list.stream().collect(Collectors.toMap(e -> e, e -> 1, (a, b) -> a + b)).entrySet().stream()
	.filter(entry -> entry.getValue() > 1) // 过滤出元素出现次数大于 1 的 entry
	.map(Map.Entry::getKey) // 获得 entry 的键（重复元素）对应的 Stream
	.collect(Collectors.toList());// 转化为 List
System.out.println(result);
```
