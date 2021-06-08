## IO文件流

* 读取文件

> 文件
```
###unit1###
a Good!
b Good after
c Good even
###unit2###
a Hello
b Hello world!
c Hi
###unit3###
a 中国
b 美国
c 法国
d 澳大利亚
```

> 代码

```java
package com.groovy;

import java.util.*;
import java.io.*;

import java.io.BufferedReader;

/**
 * @author Jonny
 * @description FileTest
 * @date 2019/10/14 0014
 */
public class FileDemo {

    public static void main(String[] args) {
        File file = new File("d:/file.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String next, line = reader.readLine();
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            List<String> sons = null;
            int i = 1;
            for (boolean first = true, last = (line == null); !last; first = false, line = next) {
                last = ((next = reader.readLine()) == null);
                System.out.println(line);
                // 去除空行
                if (line != null && line.trim().length() > 0) {
                    if (first) {
                        sons = new ArrayList<String>();
                    } else if (last) {
                        sons.add(line);
                        map.put("unit" + i, sons);
                    } else {
                        if (line.startsWith("###")) {
                            map.put("unit" + i, sons);
                            i++;
                            sons = new ArrayList<String>();
                        } else {
                            sons.add(line);
                        }
                    }
                }
            }
            System.out.println(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException logOrIgnore) {
            }
        }
    }
}
```

>结果`{unit1=[a Good!, b Good after, c Good even], unit2=[a Hello, b Hello world!, c Hi], unit3=[a 中国, b 美国, c 法国, d 澳大利亚]}`



### 优化写法

```java
public static void readLine(String fileName, Consumer<String> lineConsumer) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        String line;
        while (Objects.nonNull(line = reader.readLine())) {
            lineConsumer.accept(line);
        }
    } catch (IOException e) {
        String message = String.format("读取文件(%s)异常", fileName);
        log.error(message, e);
    }
}

public static void main(String[] args) {
    // 使用代码
    readLine("C:\\projects\\2021-06-03.0.log", line -> {
        System.out.println(line);
    });
}
```

