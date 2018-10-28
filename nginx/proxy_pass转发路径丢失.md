### 应用场景

> 当我访问`blog.first-blood.cn`的时候，proxy_pass转发到`jonny023.github.io`这个域名下去了，而`jonny023.github.io/upload/hello.jpg`这个文件我通过`blog.first-blood.cn/upload/hello.jpg`访问就出现404

### 解决办法

* 此时在nginx的配置文件下的location配置下面添加一句

```
proxy_set_header Host jonny023.github.io;
```

## 注意知道对不对，如果不对请来砍我

```
server {
	listen 80;
	server_name blog.first-blood.cn;
	charset utf-8,gbk;
	root html;  
	index index.html index.htm;
	location / {
		proxy_set_header Host jonny023.github.io;
		#proxy_set_header Host           $host;
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
	
	location ~ .*\.(js|css|eot|otf|ttf|woff|woff2)?$ {
		proxy_pass https://jonny023.github.io;
		charset utf-8,gbk;
		expires      12h;
	}
	
	location ~* \.(eot|otf|ttf|woff|woff2)$ {
		add_header Access-Control-Allow-Origin *;
	}
}
```
