## jdk8

> jdk版本版本

```sh
java version "1.8.0_301"
Java(TM) SE Runtime Environment (build 1.8.0_301-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.301-b09, mixed mode)
```

### Springboot连接mysql报错

```sh
The server selected protocol version TLS10 is not accepted by client preferences [TLS12]
```

## 方案1 修改jdk配置

```sh
# cd %JAVA_HOME%\jre\lib\security
# 打开java.security
# 搜索jdk.tls.disabledAlgorithms=
# 去除掉TLSv1, TLSv1.1,3DES_EDE_CBC
```

## 方案2 配置VM Options

```sh
"-Djdk.tls.disabledAlgorithms=SSLv3, TLSv1, 3DES_EDE_CBC, TLSv1.1, RC4, DES, MD5withRSA, DH keySize < 1024, EC keySize < 224, 3DES_EDE_CBC, anon, NULL"
```
