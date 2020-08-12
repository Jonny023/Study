> 数值型字符排序(传统方式)

```java
public static void main(String[] args) {
    List list = Arrays.asList("2", "1", "3","200","100","-100","0");

    Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Integer.valueOf(o1) - Integer.valueOf(o2);
        }
    };

    list.sort(comparator);
    System.out.println(list);
    // 输出[-100, 0, 1, 2, 3, 100, 200]
}
```

> Lambda表达式

```java
public static void main(String[] args) {
    List list = Arrays.asList("2", "1", "3","20","10000","0");

    // 升序
    // list.sort((o1, o2) -> Integer.valueOf(String.valueOf(o1)).compareTo(Integer.valueOf(String.valueOf(o2))));
    // list.sort((o1, o2) -> Integer.valueOf(String.valueOf(o1)) - Integer.valueOf(String.valueOf(o2)));

    // 不能对负数排序
    list.sort(Comparator.comparing(String::hashCode));

    // 降序
    // list.sort((o1, o2) -> Integer.valueOf(String.valueOf(o2)).compareTo(Integer.valueOf(String.valueOf(o1))));
    
    // 可以包含负整数
    List list2 = Arrays.asList("2", "1", "3","20","10000","500","-1","800");
    list2.sort(Comparator.<String,Integer>comparing(Integer::valueOf).reversed());
    System.out.println(list2);

    System.out.println(list);
}
```

> join前后加字符

```java
String[] strs = new String[]{ "a", "b", "c"};
Stream<String> strs1 = Stream.of(strs);
String collect = strs1.collect(Collectors.joining("\",\"", "\"", "\""));
System.out.println(collect);
```

# JSONArray数据筛选求和

> 求时长的和

```java
@Test
public void count() {
    String json = "[\n" +
            "  [\n" +
            "    {\n" +
            "      \"label\": \"时长(天)\",\n" +
            "      \"required\": true,\n" +
            "      \"name\": \"DURATION\",\n" +
            "      \"type\": \"number\",\n" +
            "      \"value\": \"0.5\",\n" +
            "      \"dateFormatType\": 0,\n" +
            "      \"editable\": false\n" +
            "    },\n" +
            "    {\n" +
            "      \"label\": \"同行人员\",\n" +
            "      \"required\": false,\n" +
            "      \"name\": \"COLLEAGUES\",\n" +
            "      \"type\": \"text\",\n" +
            "      \"editable\": false\n" +
            "    }\n" +
            "  ],\n" +
            "  [\n" +
            "    {\n" +
            "      \"label\": \"时长(天)\",\n" +
            "      \"required\": true,\n" +
            "      \"name\": \"DURATION\",\n" +
            "      \"type\": \"number\",\n" +
            "      \"value\": \"0.9\",\n" +
            "      \"dateFormatType\": 0,\n" +
            "      \"editable\": false\n" +
            "    },\n" +
            "    {\n" +
            "      \"label\": \"同行人员\",\n" +
            "      \"required\": false,\n" +
            "      \"name\": \"COLLEAGUES\",\n" +
            "      \"type\": \"text\",\n" +
            "      \"editable\": false\n" +
            "    }\n" +
            "  ]\n" +
            "]\n" +
            "\n";

    JSONArray jsonArray = JSON.parseArray(json);
    Double sum = jsonArray.stream().map(obj -> ((JSONArray) obj).stream().filter(arr -> {
        return "DURATION".equals(((JSONObject) arr).getString("name"));
    }).mapToDouble(arr -> Double.parseDouble(((JSONObject) arr).getString("value")))).collect(Collectors.toList()).stream().flatMapToDouble(d -> d).sum();
    System.out.println(sum);
}
```

* 字符串转set

```java
List<Map<String, Object>> list= new ArrayList<>();

Map<String, Object> map1 = new HashMap<>();
map1.put("id", 1);
map1.put("managerName", "1,1,2");

Map map2 = new HashMap<>();
map2.put("id", 2);
map2.put("managerName", "2,2,2,3");

list.add(map1);
list.add(map2);

Set<String> managerName = list.stream().map(map -> (String) map.get("managerName")).map(s -> s.split(",")).flatMap(arr -> Arrays.stream(arr)).collect(Collectors.toSet());
System.out.println(managerName);
```
