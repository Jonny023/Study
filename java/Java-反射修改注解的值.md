* 通过反射修改注解的值

* 目标类

```java
@Data
public class DynamicBaseProperties {
    @ExcelProperty(index = 3)
    String value;
}
```

* 代码
```java
  int index = 2;
  DynamicBaseProperties baseProperties = new DynamicBaseProperties();

  //获取BaseProperties的value字段
  Field field = baseProperties.getClass().getDeclaredField("value");
  //获取val字段上的Foo注解实例
  ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
  //获取 excelProperty 这个代理实例所持有的 InvocationHandler
  InvocationHandler handler = Proxy.getInvocationHandler(excelProperty);
  // 获取 AnnotationInvocationHandler 的 memberValues 字段
  Field declaredField = handler.getClass().getDeclaredField("memberValues");
  // 打开权限
  declaredField.setAccessible(true);
  // 获取 memberValues
  Map<String, Object> memberValues = (Map) declaredField.get(handler);
  //获取注解默认值
//        int oldValue = (int) memberValues.get("index");

  // 修改 value 属性值
  memberValues.put("index", index);
```
