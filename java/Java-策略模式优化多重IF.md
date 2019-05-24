# 多重if判断优化（策略模式）

```java
private static Map<String, LoadCellValue> handlerMap = new HashMap<>(7);
// 字典结合策略模式简化代码
static{
    handlerMap.put("String", new StringHandler());
    handlerMap.put("Date", new DateHandler());
    handlerMap.put("Boolean", new BooleanHandler());
    handlerMap.put("Long", new LongHandler());
    handlerMap.put("Integer", new IntegerHandler());
    handlerMap.put("Double", new DoubleHandler());
    handlerMap.put("Float", new FloatHandler());
}
HSSFCell cell = handlerMap.get(temp.getClass().getSimpleName()).loadValue(row, index, temp);

```
