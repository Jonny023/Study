# gmssl生成证书

```sh
# 拉取镜像
docker pull weiliy/gmssl
docker tag weiliy/gmssl:latest  weiliy/gmssl-x86:latest

docker pull --platform=linux/amd64 weiliy/gmssl

docker pull --platform=linux/arm64 weiliy/gmssl

# 添加下列命令别名到~/.bashrc或~/.zshrc
alias gmssl='docker run --rm -it -u $(id -u) -v "$PWD:/mnt" -v "/etc/passwd:/etc/passwd:ro" -w /mnt weiliy/gmssl gmssl'

# 生成 SM2 私钥：
docker run --rm \
-v $(pwd):/work \
-w /work \
weiliy/gmssl \
gmssl ecparam \
-genkey \
-name sm2p256v1 \
-out sm2.pem

# 查看私钥
docker run --rm \
-v $(pwd):/work \
-w /work \
weiliy/gmssl \
gmssl pkey \
-in sm2.pem \
-text

# 导出：
docker run --rm \
-v $(pwd):/work \
-w /work \
weiliy/gmssl \
gmssl pkey \
-in sm2.pem \
-pubout \
-out sm2.pub

# 生成 CSR：
docker run --rm \
-v $(pwd):/work \
-w /work \
weiliy/gmssl \
gmssl req \
-new \
-key sm2.pem \
-subj "/CN=test.com" \
-out sm2.csr

# 自签证书
docker run --rm \
-v $(pwd):/work \
-w /work \
weiliy/gmssl \
gmssl x509 \
-req \
-days 3650 \
-in sm2.csr \
-signkey sm2.pem \
-out sm2.crt

# 方案3：一次性生成（推荐）
# 不进入容器，直接输出到当前目录：
docker run --rm \
-v $(pwd):/work \
-w /work \
weiliy/gmssl \
sh -c '
gmssl sm2keygen -pass 123456 -out sm2.pem &&
gmssl reqgen -subj "/CN=test.com" -key sm2.pem -pass 123456 -out sm2.req &&
gmssl certgen -key sm2.pem -pass 123456 -in sm2.req -days 3650 -out sm2.crt
'
# 国密sm2
# 如果是正式生产环境，建议：
docker run --rm \
-v $(pwd):/work \
-w /work \
weiliy/gmssl \
sh -c '
gmssl ecparam \
-genkey \
-name sm2p256v1 \
-out harbor.key && \
gmssl req \
-new \
-key harbor.key \
-subj "/C=CN/ST=CQ/L=CQ/O=公司/OU=部门/CN=域名" \
-out harbor.csr && \
gmssl x509 \
-req \
-days 3650 \
-in harbor.csr \
-signkey harbor.key \
-out harbor.pem

# 构建conf
cat > nginx.conf <<'EOF'
events {
    worker_connections 1024;
}

http {
    server {
        listen 443 ssl;
        server_name localhost;

        ssl_certificate     /etc/nginx/cert/ssl.pem;
        ssl_certificate_key /etc/nginx/cert/ssl.key;

        location / {
            return 200 "HTTPS OK\n";
        }
    }
}
EOF

# 运行测试
docker rm -f nginx-ssl-test
docker run -d \
--name nginx-ssl-test \
-p 1993:443 \
-v $(pwd)/ssl.pem:/etc/nginx/cert/ssl.pem:ro \
-v $(pwd)/ssl.key:/etc/nginx/cert/ssl.key:ro \
-v $(pwd)/nginx.conf:/etc/nginx/nginx.conf:ro \
nginx:1.29.8

# 注释
C   国家
ST  省份
L   城市
O   公司
OU  部门
CN  域名/IP

# 自签证书
# -days 730 指定有效期2年
docker run --rm \
-v $(pwd):/work \
-w /work \
alpine/openssl:latest \
req \
-x509 \
-newkey rsa:2048 \
-nodes \
-keyout ssl.key \
-out ssl.pem \
-days 730 \
-utf8 \
-subj "/C=CN/ST=CQ/L=CQ/O=公司/OU=部门/CN=域名"

# 提供给授信机构的csr请求文件
docker run --rm \
-v $(pwd):/work \
-w /work \
alpine/openssl:latest \
req \
-newkey rsa:2048 \
-nodes \
-keyout ssl.key \
-out ssl.csr \
-days 730 \
-utf8 \
-subj "/C=CN/ST=CQ/L=CQ/O=公司/OU=部门/CN=域名"
```

