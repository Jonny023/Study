# 集合数组操作

* 判断数组是否相等

```java
int[] ary = {1,2,3,4,5,6};
int[] ary1 = {1,2,3,4,5,6};
int[] ary2 = {1,2,3,4};
System.out.println("数组 ary 是否与数组 ary1相等? ："+ Arrays.equals(ary, ary1));
System.out.println("数组 ary 是否与数组 ary2相等? ："+Arrays.equals(ary, ary2));
```

* 数组转集合

    * 该方法返回定长的 List，不支持 add 和 remove 操作
    * 该方法返回的 List 与传入数组是映射关系（视图）：set/get 操作直接作用于数组；直接修改数组，list 也会改变


> 慎用`Arrays.asList()`，因为他是`final`修饰的，是`Arrays`类中的一个静态内部类，源码如下：

```java
private static class ArrayList<E> extends AbstractList<E>
        implements RandomAccess, java.io.Serializable
    {
        private static final long serialVersionUID = -2764017481108945198L;
        private final E[] a;

        ArrayList(E[] array) {
            a = Objects.requireNonNull(array);
        }

```

```java
// 方案一
// List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
Integer[] arr = {1, 2, 3};
List<Integer> list = new ArrayList<>(Arrays.asList(arr));

// 方案二
List<Integer> list = new ArrayList<>();
// Collections.addAll(list, 1, 2, 3);
Integer[] arr = {1, 2, 3};
Collections.addAll(list, arr);

// 方案三（需 JDK8）
// List<Integer> list = Arrays.stream(new Integer[]{1, 2, 3}).collect(Collectors.toList());
int[] arr = {1, 2, 3};
List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());

// 方案四（需 Guava）
// List<Integer> list = Lists.newArrayList(1, 2, 3);
int[] arr = {1, 2, 3};
List<Integer> list = Lists.newArrayList(Ints.asList(arr));
```
