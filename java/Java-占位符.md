# 占位字符替换工具
### String.format()
  * %s 字符型
  * %d 数字型
  * %f 浮点型
  
```java
String str = "%s或%s不能为空";
System.out.println(String.format(str,"用户名","密码")); 
// username或password不能为空

String num = "%d大于%d";
System.out.println(String.format(num,10,8));
// 10大于8

String f = "%f小于%f";
System.out.println(String.format(f,0f,0.1f));
// 0.000000小于0.100000
```

### MessageFormat.format()
  * {0}xxx{1}xxx  {0}占位符号

```java
String message = "{0}或{1}不能为空";
System.out.println(MessageFormat.format(message,"username","password"));
// 用户名或密码不能为空
```

### 日期匹配替换

```java
// no.1
List<String> lists = Arrays.asList("2012-11-20", "2020-7-14", "2020-8-9");
List<String> returnData = new ArrayList<>();
StringBuffer sb = null;
String[] arr = null;
for (String s : lists) {
    if (!s.matches("\\d{4}-\\d{2}-\\d{2}")) {
        sb = new StringBuffer();
        arr = s.split("-");
        sb.append(arr[0]);
        sb.append("-");
        sb.append(String.format("%02d", Integer.parseInt(arr[1])));
        sb.append("-");
        sb.append(String.format("%02d", Integer.parseInt(arr[2])));
        s = sb.toString();
    }
    returnData.add(s);
}
System.out.println("转回前：" + lists);
System.out.println("转换后：" + returnData);

// 转回前：[2012-11-20, 2020-7-14, 2020-8-9]
// 转换后：[2012-11-20, 2020-07-14, 2020-08-09]


// no.2
List<String> results = lists.stream().map(s -> {
    if (!s.matches("\\d{4}-\\d{2}-\\d{2}")) {
        String[] strs = s.split("-");
        return strs[0] + "-" + String.format("%02d", Integer.parseInt(strs[1])) + "-" + String.format("%02d", Integer.parseInt(strs[2]));
    } else {
        return s;
    }
}).collect(Collectors.toList());
System.out.println(results); //[2012-11-20, 2020-07-14, 2020-08-09]
```
