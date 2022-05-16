# poi读取数值类型问题

```java
//cell.setCellType(CellType.STRING)虽然能解决整数带有小数点问题，但是同时也带来了原本
//小数精度丢失问题，如0.1306变成了0.131，推荐用下面的方法
DataFormatter dataFormatter = new DataFormatter();
//HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
String value = dataFormatter.formatCellValue(cell);
```
