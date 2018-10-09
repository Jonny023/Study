### 通过jdk自带方法获取resources路径

```
this.getClass().getResource("/").getPath();
```

* 例子

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

### 通过spring获取resources路径(需要spring-core核心包)

```
Resource resource = new ClassPathResource("/file");
```

* 例子

```
    Resource resource = new ClassPathResource("/file");
    try {
        System.out.println(resource.getFile().getPath());
    } catch (IOException e) {
        e.printStackTrace();
    }
```
