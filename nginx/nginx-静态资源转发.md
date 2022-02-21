* 映射

```nginx
server {
    listen       80;
    server_name  localhost;

    #charset koi8-r;

    #access_log  logs/host.access.log  main;

    location / {
        root   html;
        index  index.html index.htm;
    }

    location /download.html {
        proxy_pass http://localhost:8081/gdapp/download.html;
    }

    location ~/app_assets/.*\.(woff|ttf|jpg|gif|png|js|css)$ {
        rewrite ^/(.*)$ http://localhost:8081/gdapp/$1 permanent;
    }
}
```
