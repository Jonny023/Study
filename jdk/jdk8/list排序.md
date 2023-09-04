# 排序

### list排序

```java
Usr usr1 = new Usr("张三", null);
Usr usr2 = new Usr("王五", 2);
Usr usr3 = new Usr("李四", 1);
ArrayList<Usr> usrs = Lists.newArrayList(
        usr1, usr2, usr3
);
System.out.println(usrs);//[A.Usr(name=张三, sortValue=null), A.Usr(name=王五, sortValue=2), A.Usr(name=李四, sortValue=1)]
// 借助guava工具排序
// usrs.sort(Comparator.comparing(Usr::getSortValue, Ordering.natural().nullsLast()));

// null值排最后升序 [A.Usr(name=李四, sortValue=1), A.Usr(name=王五, sortValue=2), A.Usr(name=张三, sortValue=null)]
usrs.sort(Comparator.comparing(Usr::getSortValue, Comparator.nullsLast(Comparator.naturalOrder())));
System.out.println(usrs);

// null值在最前面升序 [A.Usr(name=张三, sortValue=null), A.Usr(name=李四, sortValue=1), A.Usr(name=王五, sortValue=2)]
usrs.sort(Comparator.comparing(Usr::getSortValue, Comparator.nullsFirst(Comparator.naturalOrder())));
System.out.println(usrs);

// null值在最后面降序 [A.Usr(name=王五, sortValue=2), A.Usr(name=李四, sortValue=1), A.Usr(name=张三, sortValue=null)]
usrs.sort(Comparator.comparing(Usr::getSortValue, Comparator.nullsLast(Comparator.reverseOrder())));
System.out.println(usrs);
```
