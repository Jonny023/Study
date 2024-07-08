
## [点我前往官方文档](https://nacos.io/zh-cn/docs/v2/quickstart/quick-start.html)

### 编译启动

```sh
git clone https://github.com/alibaba/nacos.git
cd nacos/

# 在windows powershell下执行-Dmaven.test.skip=true需要单引号引起来，不然无法识别
mvn -Prelease-nacos '-Dmaven.test.skip=true' clean install -U
mvn -Prelease-nacos -Dmaven.test.skip=true clean install -U

# 进入目录
cd distribution/target/nacos-server-$version/nacos/bin

# 若要修改配置，最好是将zip解压到其他位置后操作
cd distribution/target/nacos-server-$version.zip

# 单机启动（windows）
./startup.cmd -m standalone
```



### 2.x版本默认没有密码

* 参考：https://nacos.io/zh-cn/docs/v2/guide/user/auth.html

>  修改`conf`目录下的`application.properties`文件。设置其中的`nacos.core.auth.plugin.nacos.token.secret.key`值，详情可查看[鉴权-自定义密钥](https://nacos.io/zh-cn/docs/v2/plugin/auth-plugin.html).

```properties
# 几个必须配置的配置项
# 启用用户名密码认证
nacos.core.auth.enabled=true

# 服务间进行通信的key和秘钥，aksk
nacos.core.auth.server.identity.key=nacos
nacos.core.auth.server.identity.value=nacos

# base64编码的JWT私钥，不能少于32位
nacos.core.auth.plugin.nacos.token.secret.key=YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY3ODk=
```



### 持久化mysql

[文档](https://nacos.io/zh-cn/docs/deployment.html)

```sh
# 脚本文件
conf/mysql-schema.sql

# 修改conf/application.properties
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_dev?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai
db.user.0=root
db.password.0=123456
```

