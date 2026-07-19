# GmSSL生成证书（amd64/arm64）

## docker镜像

```bash
# 导出
docker save -o nginx-gmssl:alpine-arm.tar kiis/nginx-gmssl:alpine-arm
docker save -o nginx-gmssl:alpine-x86.tar kiis/nginx-gmssl:alpine-x86

# 导入
docker load -i nginx-gmssl\:alpine-arm.tar
docker load -i nginx-gmssl\:alpine-x86.tar
```



## amd64

### 生成CSR（分步）

```bash
# amd64架构
docker pull kiis/nginx-gmssl:alpine-x86

# 创建工作目录
mkdir -p test

# 1.生成SM2私钥
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-x86 \
gmssl sm2keygen \
-pass 123456 \
-out ssl.key

# 2.生成CSR
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-x86 \
gmssl reqgen \
-C CN \
-ST 省份 \
-L 城市 \
-O 公司 \
-OU 部门 \
-CN 域名或IP \
-key ssl.key \
-pass 123456 \
-out ssl.csr \
-days 730

# 3.解析CSR
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-x86 \
gmssl reqparse \
-in ssl.csr
```

生成文件：

```text
ssl.key
ssl.csr
```

------

### 生成CSR（一步）

```bash
# amd64架构
docker pull kiis/nginx-gmssl:alpine-x86

# 创建目录用于挂载
mkdir -p test

# 生成
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-x86 \
sh -c '
gmssl sm2keygen \
-pass 123456 \
-out ssl.key &&
gmssl reqgen \
-C CN \
-ST 省份 \
-L 城市 \
-O 公司 \
-OU 部门 \
-CN 域名或IP \
-key ssl.key \
-pass 123456 \
-out ssl.csr \
-days 730
'
```

生成文件：

```text
ssl.key
ssl.csr
```

------

### 生成自签名证书（CRT）（分步）

```bash
# amd64架构
docker pull kiis/nginx-gmssl:alpine-x86

# 创建工作目录
mkdir -p test

# 1.生成SM2私钥
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-x86 \
gmssl sm2keygen \
-pass 123456 \
-out ssl.key

# 2.生成自签名证书
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-x86 \
gmssl certgen \
-C CN \
-ST 省份 \
-L 城市 \
-O 公司 \
-OU 部门 \
-CN 域名或IP \
-days 730 \
-key ssl.key \
-pass 123456 \
-key_usage digitalSignature \
-out ssl.crt

# 3.解析证书
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-x86 \
gmssl certparse \
-in ssl.crt
```

生成文件：

```text
ssl.key
ssl.crt
```

------

### 生成自签名证书（CRT）（一步）

```bash
# amd64架构
docker pull kiis/nginx-gmssl:alpine-x86

mkdir -p test

docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-x86 \
sh -c '
gmssl sm2keygen \
-pass 123456 \
-out ssl.key &&

gmssl certgen \
-C CN \
-ST 省份 \
-L 城市 \
-O 公司 \
-OU 部门 \
-CN 域名或IP \
-days 730 \
-key ssl.key \
-pass 123456 \
-key_usage digitalSignature \
-out ssl.crt
'
```

生成文件：

```text
ssl.key
ssl.crt
```

------

## arm64

### 生成CSR（分步）

```bash
# arm64架构
docker pull kiis/nginx-gmssl:alpine-arm

# 创建工作目录
mkdir -p test

# 1.生成SM2私钥
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-arm \
gmssl sm2keygen \
-pass 123456 \
-out ssl.key

# 2.生成CSR
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-arm \
gmssl reqgen \
-C CN \
-ST 省份 \
-L 城市 \
-O 公司 \
-OU 部门 \
-CN 域名或IP \
-key ssl.key \
-pass 123456 \
-out ssl.csr

# 3.解析CSR
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-arm \
gmssl reqparse \
-in ssl.csr
```

生成文件：

```text
ssl.key
ssl.csr
```

------

### 生成CSR（一步）

```bash
# arm64架构
docker pull kiis/nginx-gmssl:alpine-arm

mkdir -p test

docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-arm \
sh -c '
gmssl sm2keygen \
-pass 123456 \
-out ssl.key &&

gmssl reqgen \
-C CN \
-ST 省份 \
-L 城市 \
-O 公司 \
-OU 部门 \
-CN 域名或IP \
-key ssl.key \
-pass 123456 \
-out ssl.csr
'
```

生成文件：

```text
ssl.key
ssl.csr
```

------

### 生成自签名证书（CRT）（分步）

```bash
# arm64架构
docker pull kiis/nginx-gmssl:alpine-arm

# 创建工作目录
mkdir -p test

# 1.生成SM2私钥
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-arm \
gmssl sm2keygen \
-pass 123456 \
-out ssl.key

# 2.生成自签名证书
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-arm \
gmssl certgen \
-C CN \
-ST 省份 \
-L 城市 \
-O 公司 \
-OU 部门 \
-CN 域名或IP \
-days 730 \
-key ssl.key \
-pass 123456 \
-key_usage digitalSignature \
-out ssl.crt

# 3.解析证书
docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-arm \
gmssl certparse \
-in ssl.crt
```

生成文件：

```text
ssl.key
ssl.crt
```

------

### 生成自签名证书（CRT）（一步）

```bash
# arm64架构
docker pull kiis/nginx-gmssl:alpine-arm

mkdir -p test

docker run --rm \
-v $(pwd)/test:/work \
-w /work \
kiis/nginx-gmssl:alpine-arm \
sh -c '
gmssl sm2keygen \
-pass 123456 \
-out ssl.key &&

gmssl certgen \
-C CN \
-ST 省份 \
-L 城市 \
-O 公司 \
-OU 部门 \
-CN 域名或IP \
-days 730 \
-key ssl.key \
-pass 123456 \
-key_usage digitalSignature \
-out ssl.crt
'
```

生成文件：

```text
ssl.key
ssl.crt
```
