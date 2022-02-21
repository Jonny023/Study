## 前后端分离部署

### nginx.conf

```nginx
server {
  listen 80;
  server_name localhost;
  index index.html;
  root /usr/local/project/test;

  gzip on;
  gzip_vary on;
  gzip_buffers 32 4K;
  gzip_comp_level 6;
  gzip_min_length 100;
  gzip_types application/javascript text/css text/xml;
  gzip_disable "MSIE [1-6]\.";

  location / {
    try_files $uri $uri/ @router;
  }

  location @router {
    rewrite ^.*$ /index.html last;
  }

  location /basic-api {
    proxy_pass http://localhost:8080/api;
  }

  location ~ .*＼.(gif|jpg|jpeg|png|bmp|swf)$ {
    expires       15d;
  }

  location ~ .*＼.(js|css)$ {
    expires       7d;
  }
}

```

