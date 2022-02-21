> 通过nginx实现访问80端口跳转到同时带有端口和项目名的服务器
* 当浏览器访问localhost时，想让域名服务器访问服务器http://192.168.1.10:9999/test这个项目
* 通过域名访问时不想加端口，也不想加项目名
```nginx
server {
    listen       80;
    server_name  localhost;
     
    location / {
        proxy_pass http://192.168.1.10:9999/test/;
        proxy_set_header   Host    $host;
        proxy_set_header   X-Real-IP   $remote_addr;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    location /test/ {
        proxy_pass http://192.168.1.10:9999/test/;
        proxy_set_header   Host    $host;
        proxy_set_header   X-Real-IP   $remote_addr;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```
