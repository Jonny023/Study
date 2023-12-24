* 在运维的时候，一般是将上传的文件存放到tomcat下的webapp下面，但是会有让人很困扰的问题，当我要迁移tomcat到其他的地方的时候会非常麻烦，文件很大，拷贝起来也灰常慢，这个时候我们可以配置tomcat虚拟路径类将文件隐射到指定目录，将文件存放到非tomcat下面的其他磁盘，配置：

```xml
<Host appBase="webapps" autoDeploy="true" name="localhost" unpackWARs="true">
      <Context path="/test01/uploadFiles" docBase="d:\upload"  debug="0"  reloadable="true"></Context> 
      <!-- 若不行docBase后面加上\或/ -->
      <!-- <Context path="/test01/uploadFiles" docBase="d:\upload\"  debug="0"  reloadable="true"></Context> -->
</Host>
```

* 参数说明：

```
path: 文件访问前缀，访问的时候 http://localhost:8080/test01/uploadFiles/xxx.jpg，如果path为空，则访问url为：http://localhost:8080/xxx.jpg
docBase: 文件存放的路径，可以用绝对路径，如果你直接配置doc="/xxx"，则映射的是当前tomcat所在磁盘的根路径
```
如果项目有权限控制，则需要特别注意配置好path，如果path为空，任何人都可以访问到你的文件，因为文件和你的tomcat相当于是兄弟关系，并不是父子关系，所以权限控制不到文件的

### 404请[参考](https://blog.csdn.net/istend/article/details/52892208)


### 文件虚拟路径映射

```xml
<Context path="/img" docBase="G:\photo" reloadable="true"/>
```

* 项目访问就是：localhost:8080/img/file.png
