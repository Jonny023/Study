## 获取springboot应用路径

```java
//打包也能获取
ApplicationHome applicationHome = new ApplicationHome(DemoApp.class);
LOGGER.warn("{}", applicationHome.getDir());
LOGGER.warn("{}", applicationHome.getSource());

//没打包时获取输出：D:\deomapp\target\classes
//打包后输出：D:\deomapp\target\demoapp.jar




//打包后无法获取，返回null
String jarPath = DemoApp.class
    			.getProtectionDomain()
                .getCodeSource()
      			.getLocation()
                .toURI()
                .getPath();
//没打包时输出：/D:/deomapp/target/classes
```

