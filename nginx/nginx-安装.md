### 安装准备

```bash
yum -y install gcc gcc-c++ autoconf automake
yum -y install zlib zlib-devel openssl openssl-devel pcre-devel
```

* nginx 下载地址 http://nginx.org/en/download.html

```bash
//下载     网络不通 请在 windows上 下载后 用rz 上传
wget http://nginx.org/download/nginx-1.13.9.tar.gz
//解压
tar zxvf nginx-1.13.9.tar.gz
//进入nginx目录
cd nginx-1.13.9
//编译   安装  
./configure --prefix=/usr/local/nginx --with-http_stub_status_module --with-http_ssl_module
make 
sudo make install

//软链接 这样不管在哪里都可以直接使用 nginx命令不需要进入 /usr/local/nginx/sbin目录
sudo ln -s /usr/local/nginx/sbin/nginx /usr/bin/nginx

```
