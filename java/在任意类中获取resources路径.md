* 获取resources路径

```
this.getClass().getResource("/").getPath();
```

#### 例子

```
package a;

import java.io.File;

public class T {

    static String path = T.class.getClass().getResource("/").getPath();

    public static void main(String[] args) {
        System.out.println(path);
        File file = new File(path + "a.properties");
        System.out.println(file.isFile());
        System.out.println(file.exists());
    }
}

```
