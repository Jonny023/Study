# Lambda表达式

> 数据类型转换(将`List<Long>`转为`Set<String>`)

```java
public static void main(String[] args) {
    List<Long> longs = Arrays.asList(1L, 2L, 2L, 10L, 11L, 1000L, 20L, 10L);
    System.out.println(longs);
    Set<Long> sets = longs.stream().collect(Collectors.toSet());
    // 方式一
    Set<String> strings = sets.stream().map(v -> String.valueOf(v)).collect(Collectors.toSet());
    System.out.println(strings);

    // 方式二
    Set<String> strs = longs.stream().map(v -> String.valueOf(v)).collect(Collectors.toSet());
    System.out.println(strs);
}
```

```java
package com.test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Lee
 * @Description
 * @Date 2019年05月19日 21:03
 */
public class HelloTest {

    public static void main(String[] args) {

        List<Student> students = Stream.of(new Student("张三", '男', 20),
                new Student("李四", '男', 22),
                new Student("李四", '男', 23),
                new Student("小丽", '女', 18)).collect(Collectors.toList());
        System.out.println(students);

        // filter过滤满足条件的数据
        List<Student> stus = students.stream().filter(stu -> stu.getAge() > 18).collect(Collectors.toList());
        System.out.println(stus);

        // 获取满足条件的字符集合
        List<String> names = students.stream().filter(stu -> stu.getAge() > 18).map(Student::getName).collect(Collectors.toList());
        System.out.print(names);
        System.out.println("");

        // map返回指定数据集合
        List<String> lists = students.stream().map(stu -> stu.getName()).collect(Collectors.toList());
        System.out.println(lists);

        // 将多个Stream合并为一个
        List<Student> students1 = Stream.of(new Student("小舞", '女', 19), new Student("小花", '女', 19)).collect(Collectors.toList());
        List<Student> allStudents = Stream.of(students, students1).flatMap(s -> s.stream()).collect(Collectors.toList());
        System.out.println(allStudents);

        // 获取最大值最小值
        Optional<Student> min = allStudents.stream().min(Comparator.comparing(stu-> stu.getAge()));
        Optional<Student> max = allStudents.stream().max(Comparator.comparing(stu-> stu.getAge()));
        // 判断是否有值
        if(min.isPresent()) {
            System.out.printf("最小年龄为：%d \n", min.get().getAge());
        }
        if(min.isPresent()) {
            System.out.printf("最大年龄为：%d \n", max.get().getAge());
        }
        //当值为null时使用指定值
        max.orElse(new Student("默认", '男', 20));
        max.orElseGet(() -> new Student("默认", '男', 20));
        System.out.println(max);

        // count统计
        long count = allStudents.stream().filter(stu-> stu.getSex() == '女').count();
        System.out.println(count);

        // reduce一组值生成一个值
        Integer reduce = Stream.of(1, 3, 5, 7).reduce(0, (m, n) -> (m + n));
        System.out.printf("和为：%d \n", reduce);

        // 平均值
        double avgAge = allStudents.stream().collect(Collectors.averagingInt(Student::getAge));
        System.out.printf("平均年龄：%f \n", avgAge);

        // 获取年龄最大的学生信息
        Stream<Student> studentStream = allStudents.stream();
        System.out.println(studentStream.collect(Collectors.maxBy(Comparator.comparing(s-> s.getAge()))));
        System.out.println(studentStream);

        // 将男女生分组
        Map<Boolean, List<Student>> types = allStudents.stream().collect(Collectors.partitioningBy(stu-> '男' == stu.getSex()));
        System.out.println(types.get(true));
        System.out.printf("男生人数：%d \n", types.get(true).stream().count());
        System.out.println(types.get(false));
        System.out.printf("女生人数：%d \n", types.get(false).stream().count());

        // 按照年龄分组
        Map<Integer, List<Student>> groupStudents = allStudents.stream().collect(Collectors.groupingBy(stu-> stu.getAge()));
        System.out.println(groupStudents);

        // 字符拼接
        String nameStrs = allStudents.stream().map(Student::getName).collect(Collectors.joining(","));
        System.out.println(nameStrs);
        // joining第一个是分界符，第二个是前缀符，第三个是结束符
        String nameStrs1 = allStudents.stream().map(Student::getName).collect(Collectors.joining(",", "[", "]"));
        System.out.println(nameStrs1);



    }

    static class Student {
        private String name;
        private char sex;
        private Integer age;

        public Student() {}

        public Student(String name, char sex, Integer age) {
            this.name = name;
            this.sex = sex;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public char getSex() {
            return sex;
        }

        public void setSex(char sex) {
            this.sex = sex;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", sex=" + sex +
                    ", age=" + age +
                    '}';
        }
    }

}

```
