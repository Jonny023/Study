## 配置文件优先级从高到低顺序↓

+ `file:./config/`  - 优先级最高（项目根路径下的`config`）
+ `file:./`  - 优先级第二  -（项目根路径下）
+ `classpath:/config/`  - 优先级第三（项目`resources/config`下）
+ `classpath:/`  - 优先级第四（项目`resources`根目录）

> SpringBoot项目启动会去扫面项目以上目录位置的`application.yml`或`application.properties`文件

#### 以上位置的`application.yml`或`application.properties`遵循：

* `高优先级配置`会覆盖`低优先级配置`

* 多个配置文件`互补`

  + 比如，两个同名文件里面有相同的配置，相同的配置会被高优先级的配置覆盖

  > A配置优先级大于B配置

  ```yaml
  server:
  	port: 8080
  ```

  > B配置优先级小于A配置

  ```yaml
  server: 
  	port: 8081
  	context-path: /test
  ```

  项目启动后访问地址为：`http://localhost:8080/test`，这就是所谓的`互补`

  

---



#### 项目打包运行后可通过命令指定配置文件位置

```bash
--spring.config.location=d:/application.properties
```

* 实例

```bash
java -jar demo-xxx.jar --spring.config.location=d:/application.properties
```

> 这对于运维来说非常方便，在不破坏原配置情况下轻松修改少量配置就可以达到想要的效果