## 本地域名映射

* 修改C:\Windows\System32\drivers\etc下的hosts文件，在hosts中添加

```
127.0.0.1       www.cms.com
```

## nginx配置集群配置

* 修改nginx.conf文件

```
http {
	
	upstream local {
        server 127.0.0.1:8889;
        server 127.0.0.1:8880;
        ip_hash;  # 解决session共享问题
  }
	
	server {
	
        listen       80;
        server_name  localhost;
			
		location / {
			charset utf-8,gbk;  #解决中文乱码问题。
			proxy_pass http://local;
		}
	}
  
}
```
