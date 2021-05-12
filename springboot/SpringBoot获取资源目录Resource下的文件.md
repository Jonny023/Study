# SpringBoot获取资源目录resources下的文件

> 文件存放到`resources/db`下，如：`resource/db/ip2region.db`，试过其他方式要么报错，要么打包jar后报错

```java
ClassPathResource resource = new ClassPathResource("db/ip2region.db");
System.out.println(resource.getFile().getPath());
System.out.println(resource.getInputStream());
```