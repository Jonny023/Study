## 多字段排序

### 实体类

```java
package com.example.demo.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student {

    private Long id;
    private String stuName;
    private Integer age;
    private Double score;
    private Integer height;

}
```

### 示例

```java
package com.example.demo.test;

import com.example.demo.domain.entity.Student;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.lang.System.out;

public class Demo {

    public static final String AGE = "age";
    public static final String HEIGHT = "height";
    public static final String SCORE = "score";

    @Data
    @Builder
    public static class Sort {
        /**
         * 排序字段
         */
        private String fieldName;
        /**
         * 排序规则：asc/desc
         */
        private SortEnum sort;

        public enum SortEnum {
            ASC,DESC
        }
    }

    /**
     *  多字段动态排序
     */
    @Test
    public void exe() {
        List<Student> list = getList();
        List<Sort> sorts = getSorts();
        Comparator<Student> comparator = null;
        int count = 0;
        for (Sort sort : sorts) {
            if (count == 0) {
                comparator = firstSort(sort);
            } else {
                thenOtherSort(comparator, sort);
            }
            count++;
        }

        out.println("原集合: " + list);

        //排序
        list.sort(comparator);
        out.println("排序后的集合: " + list);
    }

    /**
     *  其他排序
     * @param comparator
     * @param sort
     */
    private void thenOtherSort(Comparator<Student> comparator, Sort sort) {
        switch (sort.getFieldName()) {
            case AGE:
                comparator = comparator.thenComparing(Student::getAge);
                break;
            case HEIGHT:
                comparator = comparator.thenComparing(Student::getHeight);
                break;
            case SCORE:
                comparator = comparator.thenComparing(Student::getScore);
                break;
            default:
                break;
        }
        if (Sort.SortEnum.DESC == sort.getSort()) {
            comparator = comparator.reversed();
        }
    }

    /**
     *  第一排序
     * @param sort
     */
    private Comparator<Student> firstSort(Sort sort) {
        Comparator<Student> comparator = null;
        switch (sort.getFieldName()) {
            case AGE:
                comparator = Comparator.comparing(Student::getAge);
                break;
            case HEIGHT:
                comparator = Comparator.comparing(Student::getHeight);
                break;
            case SCORE:
                comparator = Comparator.comparing(Student::getScore);
                break;
            default:
                break;
        }
        if (Sort.SortEnum.DESC == sort.getSort()) {
            comparator = Objects.requireNonNull(comparator).reversed();
        }
        return comparator;
    }

    private List<Sort> getSorts() {
        return Arrays.asList(
                Sort.builder().fieldName(AGE).sort(Sort.SortEnum.ASC).build(),
                Sort.builder().fieldName(SCORE).sort(Sort.SortEnum.DESC).build(),
                Sort.builder().fieldName(HEIGHT).sort(Sort.SortEnum.DESC).build()
        );
    }

    private List<Student> getList() {
        return Arrays.asList(
                Student.builder().id(1L).stuName("小张").age(20).score(80D).height(180).build(),
                Student.builder().id(1L).stuName("小明").age(22).score(94D).height(165).build(),
                Student.builder().id(1L).stuName("小慧").age(17).score(76D).height(169).build()
        );
    }
}
```

## 冒泡排序

* 循环比较前后两个值的大小，比较`n * n`次，通过中间变量交换值

```java
static int[] array(int[] ars) {
    int temp;
    int size = ars.length;
    for (int i = 1; i < size; i++) {
        for (int j = 0; j < size - 1; j++) {
            if(ars[j] > ars[j+1]) {
                temp = ars[j];
                ars[j] = ars[j+1];
                ars[j+1] = temp;
            }
        }
    }
    return ars;
}
```


## 字符排序

```java
public static void main(String[] args) {
    //正则取反：((?!正则表达式).)*$
    String regex = "((?![0-9]).)*$";
    List<String> list = Arrays.asList("12楼", "1楼", "18楼", "4楼", "5楼", "20楼");

    //升序排序
    List<String> resultList = list.stream().sorted(
            Comparator.comparing(a -> Integer.valueOf(a.replaceAll(regex, "")))
    ).collect(Collectors.toList());

    //降序排序
    List<String> descList = list.stream().sorted(
            Comparator.comparing(a -> Integer.valueOf(String.valueOf(a).replaceAll(regex, ""))).reversed()
    ).collect(Collectors.toList());

    System.out.println("原集合：" + list);
    System.out.println("正序排序后的集合：" + resultList);
    System.out.println("降序排序后的集合：" + descList);

}

@org.junit.jupiter.api.Test
public void run() {
    List<String> list = Arrays.asList("10楼", "5楼", "4楼", "12楼", "7楼", "18楼");
    List<String> resultList = list.stream().sorted(Comparator.comparingInt(String::hashCode)).collect(Collectors.toList());
    List<String> descList = list.stream().sorted(Comparator.comparingInt(String::hashCode).reversed()).collect(Collectors.toList());
    System.out.println(resultList);
    System.out.println(descList);

}
```

## 不确定集合排序的问题

> 若通过stream方式排序，如果元数据list是List<?>不确定泛型数据，排序完后不能通过引用方式传递，因为List<?>不可修改，stream返回的是新的对象，因此排序后返回的是新的集合，需要用新的变量接收

```java
package com.cnskytec.config;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {

        List<User> list = new ArrayList<>();
        list.add(User.builder().age(18).score(90D).build());
        list.add(User.builder().age(16).score(99D).build());
        list.add(User.builder().age(18).score(80D).build());

        System.out.println(list);
        System.out.println(sortDesc(list));

        System.out.println(sortAsc(list));


    }

    /**
     * 多字段同时降序
     *
     * @param list
     */
    public static List<?> sortDesc(List<?> list) {
        List<User> result = (List<User>) list;
        List<User> sortResult = result.stream().sorted(Comparator.comparing(User::getAge).reversed().thenComparing(User::getScore, Comparator.reverseOrder())).collect(Collectors.toList());
        //引用传递无效
        //list = sortResult;
        return sortResult;
    }

    /**
     * 多字段同时升序
     *
     * @param list
     */
    public static List<?> sortAsc(List<?> list) {
        List<User> result = (List<User>) list;
        return result.stream().sorted(Comparator.comparing(User::getAge).thenComparing(User::getScore)).collect(Collectors.toList());
    }


    @Data
    @Builder
    @ToString
    public static class User {

        private Integer age;
        private Double score;
    }
}
```
