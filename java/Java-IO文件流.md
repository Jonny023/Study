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
            List<String> sons = null;
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            int i = 1;
            for (boolean first = true, last = (line == null); !last; first = false, line = next) {
                last = ((next = reader.readLine()) == null);
                if (first) {
                    sons = new ArrayList<String>();
                } else if (last) {
                    sons.add(line);
                    map.put("unit"+i, sons);
                } else {
                    if (line.startsWith("###")) {
                        sons = new ArrayList<String>();
                        map.put("unit"+i, sons);
                        i++;
                    } else {
                        sons.add(line);
                    }
                }
            }
            System.out.println(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
        }
    }
}
```

>结果` {unit1=[a Hello, b Hello world!, c Hi], unit2=[a Hello, b Hello world!, c Hi]}`
