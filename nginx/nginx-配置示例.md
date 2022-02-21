```nginx
server {
    listen 80;
    server_name a.com;     

    location / {
        proxy_pass http://127.0.0.1:40000;
        proxy_set_header Host $host:80;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}

server {

    listen 40000;
    #server_name localhost;
    server_name_in_redirect off;
    
    root /home/oa;
   index index.html;
    
    location / {
        root /home/oa;
        try_files $uri $uri/ /index.html last;
        index  index.html;
    }
    
    location /gd-houtai {
        root /home/gd-houtai/;
        index  login.html  login.htm;  
    }
    
    location ~ ^/gd-houtai/.*\.(gif|jpg|jpeg|png|bmp|swf|css|js|eot|svg|ttf|woff|woff2|properties|json)?$ {
        root /home/gd-houtai/;
        expires 30d;
    }
    
    location /download.html {
       proxy_pass http://localhost:39999/gd/gdapp/download.html;
    }
    
    location ~/app_assets/.*\.(woff|ttf|jpg|gif|png|js|css)$ {
        rewrite ^/(.*)$ http://111.112.22.10:39999/gd/gdapp/$1 permanent;
    }
    
    location ~ .*\.(apk|ipa)$ {
        #指定存放路径：必须是相对路径
        root /home/app/; 
    }

    location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)$ {
        expires      30d;
    }

    location ~ .*\.(js|css)?$ {
        expires      12h;
    }

    location ~ /\. {
        deny all;
    }

    access_log  /www/wwwlogs/access.log;
    error_log  /www/wwwlogs/error.log;
}

```
