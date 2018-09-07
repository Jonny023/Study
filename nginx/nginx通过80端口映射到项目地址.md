> 通过nginx实现访问80端口跳转到同时带有端口和项目名的服务器
```
server {
    listen       80;
    server_name  crm.xxx.net;
     
    location / {
        proxy_pass http://192.168.1.216/test/;
        proxy_set_header   Host    $host;
        proxy_set_header   X-Real-IP   $remote_addr;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    location /test/ {
        proxy_pass http://192.168.1.216/test/;
        proxy_set_header   Host    $host;
        proxy_set_header   X-Real-IP   $remote_addr;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```
