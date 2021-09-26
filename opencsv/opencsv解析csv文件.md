# 读取csv

* 依赖

```xml
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>4.4</version>
</dependency>
```

* 例子

```java
public static void main(String[] args) {

    String fileName = "D:\\upload\\csv\\2021926\\template.csv";
    try {
        InputStreamReader is = new InputStreamReader(new FileInputStream(fileName), "utf-8");
        CSVParser csvParser = new CSVParserBuilder().withSeparator('\t').build();
        CSVReader reader = new CSVReaderBuilder(is).withCSVParser(csvParser).build();
        List<String[]> strings = reader.readAll();
        System.out.println();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

