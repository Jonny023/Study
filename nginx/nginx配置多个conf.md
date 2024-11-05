## nginx配置多个conf

### nginx.conf

```bash
worker_processes  auto;

http {
	server {
	
	}
	# 引入外部conf配置
	include ../ext/*.conf;
}
```



### test.com.conf

```sh
server {
	listen 80;
	server_name test.com;

	location / {
		default_type text/html;
		return 200 'test.com';
	}
}
```



### demo.com.conf

```sh
server {
	listen 80;
	server_name demo.com;

	location / {
		default_type text/html;
		return 200 'demo.com';
	}
}
```

