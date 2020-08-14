# Nginx注意事项

```
location下面的root只能配置一个，其他的配置为alias
```

* nginx
```
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
