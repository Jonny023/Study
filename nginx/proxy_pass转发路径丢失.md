> 配置如下

```
server {
    	listen 80;
        server_name blog.first-blood.cn;
        charset utf-8,gbk;
		    root html;  
    	  index index.html index.htm;
        location / {
            proxy_set_header Host jonny023.github.io; # 解决路径丢失问题
            #proxy_set_header Host            $host;
            proxy_set_header X-Real-IP       $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
			      proxy_pass https://jonny023.github.io;
        }
        
        location ^~/archives/ {
        	proxy_pass https://jonny023.github.io/archives/;
        }
        
        location ^~/uploads/ {
        	proxy_pass https://jonny023.github.io/uploads/;
        }
        
        location /favico.ico {
        	 proxy_pass https://jonny023.github.io;
             charset utf-8,gbk;
             expires      12h;
        }
        
        location ~ .*\.(js|css|eot|otf|ttf|woff|woff2)?$
        {
            proxy_pass https://jonny023.github.io;
            charset utf-8,gbk;
            expires      12h;
        }
        
        location ~* \.(eot|otf|ttf|woff|woff2)$ {
            add_header Access-Control-Allow-Origin *;
        }
}
```
