> 判断集合、数组元素是否一致

```java
//判断数组元素是否完全相同
char[] arr1 = new char[] {'A', 'B'};
char[] chars = "BA".toCharArray();
System.out.println(chars[0]);

Arrays.sort(arr1);
Arrays.sort(chars);
System.out.println(Arrays.equals(arr1, chars));

//判断集合元素是否完全相同
List<String> a = Arrays.asList("1", "8", "20");
List<String> b = Arrays.asList("1", "20", "8");

if (CollectionUtils.isEqualCollection(a, b)) {
    System.out.println("true...");
}

System.out.println(a);
System.out.println(b);

Collections.sort(a);
Collections.sort(b);

System.out.println(a.equals(b));
```
