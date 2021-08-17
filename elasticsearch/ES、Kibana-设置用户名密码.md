# ES/Kibana-设置用户名密码

[参考官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.14/security-minimal-setup.html)

1.关闭es服务和kibana服务

```shell
systemctl stop kibana.service
systemctl stop elasticsearch.service
```

2.修改配置`$ES_PATH_CONF/elasticsearch.yml`,对应`es/config/slasticsearch.yml`

```yaml
xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true
xpack.security.transport.ssl.verification_mode: certificate
xpack.security.transport.ssl.keystore.path: certs/elastic-certificates.p12
xpack.security.transport.ssl.truststore.path: certs/elastic-certificates.p12
```

3.生成ca证书

```bash
./elasticsearch-certutil ca

# 生成p12秘钥
./elasticsearch-certutil cert --ca elastic-stack-ca.p12

# 生成文件在/usr/local/elasticsearch-7.14.0路径下
/usr/local/elasticsearch-7.14.0
```

4.配置证书

```shell
cd config/
mkdir certs

chmod 755 certs && chown -R elk:elk certs
cd certs/
cp ../../elastic-certificates.p12 .
chmod 755 elastic-certificates.p12 && chown -R elk:elk elastic-certificates.p12
```



# 为内置用户创建密码

* 集群通讯需要密码，除非启用匿名配置，否则不包含`用户名`和`密码`的请求都会被拒绝
  * 只需在启用最低或基本安全性时为`elastic`和`kibana_system`用户设置密码

### 1.启动es服务

```shell
systemctl start elasticsearch.service
```

### 2.运行

```bash
# 设置用户名和密码elastic, kibana, logstash_system,beats_system
#Changed password for user [apm_system]
#Changed password for user [kibana_system]
#Changed password for user [kibana]
#Changed password for user [logstash_system]
#Changed password for user [beats_system]
#Changed password for user [remote_monitoring_user]
#Changed password for user [elastic]

./elasticsearch-setup-passwords interactive
```

> 运行报错

```shell
[root@localhost bin]# ./elasticsearch-setup-passwords
warning: usage of JAVA_HOME is deprecated, use ES_JAVA_HOME
Future versions of Elasticsearch will require Java 11; your Java version from [/usr/local/java/jdk1.8.0_291/jre] does not meet this requirement. Consider switching to a distribution of Elasticsearch with a bundled JDK. If you are already using a distribution with a bundled JDK, ensure the JAVA_HOME environment variable is not set.
Sets the passwords for reserved users

Non-option arguments:
command

Option             Description
------             -----------
-E <KeyValuePair>  Configure a setting
-h, --help         Show help
-s, --silent       Show minimal output
-v, --verbose      Show verbose output
ERROR: Missing command
```

* 需要指定java11环境

# es7需要java11

* [下载](https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_linux-x64_bin.tar.gz)

```shell
wget https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_linux-x64_bin.tar.gz
```

* 解压文件

```shell
tar xvf openjdk-11.0.1_linux-x64_bin.tar.gz -C /usr/local && mv jdk-11.0.1 java11
```

**方式1：编辑`~/.bash_profile`用户配置**

* 编辑完后执行`source ~/.bash_profile`使其生效

```bash
export JAVA_HOME=/usr/local/java11
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar


# java11
export ES_JAVA_HOME=/usr/local/java11
export PATH=$ES_JAVA_HOME/bin:$PATH
export CLASSPATH=.:$ES_JAVA_HOME/lib/dt.jar:$ES_JAVA_HOME/lib/tools.jar
```

**方式2：修改`elasticsearch`配置，建议备份下**

```shell
#配置自己的jdk11
export JAVA_HOME=/usr/local/java11
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

#添加jdk判断
if [ -x "$JAVA_HOME/bin/java" ]; then
        JAVA="$JAVA_HOME/bin/java"
else
        JAVA=`which java`
fi
```

> 将：`-XX:+UseConcMarkSweepGC`改为：`-XX:+UseG1GC`

# 验证es密码

```bash
curl localhost:9200 -u elastic:{password}

curl localhost:9200 -u elastic:123456
```



# es修改密码

> 回车后需要输入原密码，

```shell
curl -H "Content-Type:application/json" -XPOST -u elastic 'http://localhost:9200/_xpack/security/user/elastic/_password' -d '{ "password" : "123456" }'


curl -H "Content-Type:application/json" -XPOST -u kibana_system 'http://localhost:9200/_xpack/security/user/elastic/_password' -d '{ "password" : "kibana_system" }'

curl -H "Content-Type:application/json" -XPUT -u kibana_system 'http://localhost:9200/_xpack/security/user/kibana/_password' -d '{"password":"kibana_system"}'
```

### 3.在kibana配置文件`KIB_PATH_CONF/kibana.yml` 中配置用户`kibana_system` :

```yaml
elasticsearch.username: "kibana"
elasticsearch.password: "kibana"
```

### 4.重启kibana服务

```bash
systemctl restart kibana.service
```

参考文档：https://blog.51cto.com/qiangsh/2342811

```bash
curl 'http://localhost:9200/_cat/indices?pretty' -u kibana_system:kibana_system

curl 'http://localhost:9200/_cat/indices?pretty' -u kibana:kibana
```

