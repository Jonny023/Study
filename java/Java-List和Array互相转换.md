# List Array互相转换

```java 
// list -> array
// 1
changedList.stream().map(task -> String.format("hello_", task.getId())).toArray(String[]::new);

// 2 guava工具 
String[] arrays = Lists.newArrayList(list);



// array -> list
// 1
Arrays.asList(arrays);
new ArrayList(Arrays.asList(arrays));

// 2
Arrays.stream(array).boxed().collect(Collectors.toList());

// 3
Stream.of(keyList).collect(Collectors.toList())
```
