# Nginx注意事项

```
location下面的root只能配置一个，其他的配置为alias
```

* nginx
```nginx
location / {
    root   /test;
    # vue防止路由丢失，浏览器刷新地址丢失，访问不到页面
    try_files $uri $uri/ /index.html last;
    index  index.html;
}

location /admin {
    alias   /usr/test;
    # vue防止路由丢失，浏览器刷新地址丢失，访问不到页面
    try_files $uri $uri/ /index.html last;
    index  index.html;
}
```

# 软连接

```bash

ln -s /usr/local/nginx/sbin/nginx /usr/bin/nginx
```

# 重启

```bash
nginx -s reload
```

# 停止
```bash
# 快速退出
nginx -s stop

# 正常退出
nginx -s quit
```

# 指定配置启动

```bash
nginx -t -c /usr/local/nginx/conf/nginx.conf
nginx -c /usr/local/nginx/conf/nginx.conf
```
* -c 指定文件
* -t 验证配置是否正确
* -s 发送信号stop, quit, reopen, reload