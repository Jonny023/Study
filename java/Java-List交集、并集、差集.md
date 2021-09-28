# List交集、并集、差集

```java
	public static void main(String[] args) {

        // 并集 list1.addAll(list2);
        // 去重复并集 list2.removeAll(list1);list1.addAll(list2);
        // 交集 list1.retainAll(list2);
        // 差集 list1.removeAll(list2);
        List<Integer> list1 = Arrays.asList(1,2,3);
        List<Integer> list2 = Arrays.asList(3,4,5);

        //交集
        List<Integer> listA = list1.parallelStream().filter(item -> list2.contains(item)).collect(Collectors.toList());
        System.out.println("交集：");
        System.out.println(listA);

        //差集 list1不在list2中
        List<Integer> listB = list1.parallelStream().filter(item -> !list2.contains(item)).collect(Collectors.toList());
        System.out.println("差集list1-list2：");
        System.out.println(listB);

        //差集 list1不在list2中
        List<Integer> listC = list2.parallelStream().filter(item -> !list1.contains(item)).collect(Collectors.toList());
        System.out.println("差集list2-list1：");
        System.out.println(listC);

        //并集
        List<Integer> listD = new ArrayList<>();
        listD.addAll(list1);
        listD.addAll(list2);
        System.out.println("并集：");
        System.out.println(listD);

        //并集去重
        List<Integer> listE = listD.stream().distinct().collect(Collectors.toList());
        System.out.println("并集去重1：");
        System.out.println(listE);
        Set<Integer> listE1 = listE.stream().collect(Collectors.toSet());
        //Set<Integer> listE1 = new HashSet<>(listE);;
        System.out.println("并集去重2：");
        System.out.println(listE1);
    }
```