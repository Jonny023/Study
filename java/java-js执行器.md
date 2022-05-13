# 执行js代码

```java

ScriptEngineManager manager = new ScriptEngineManager();
ScriptEngine se = manager.getEngineByName("js");
String str = "1+2";
Object result;
try {
    result = se.eval(str);
    System.out.println(result);
} catch (ScriptException e) {
    e.printStackTrace();
}
```
