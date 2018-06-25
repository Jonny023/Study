具体测试步骤可参阅网络：
```
server {
    root html;  
	index index.html index.htm;
    #这里配置http2协议
    listen 443 ssl http2 default_server;
    server_name first-blood.cn www.first-blood.cn;
   
	ssl_certificate_key  /usr/local/ssl/www.first-blood.cn.key;
    ssl_certificate      /usr/local/ssl/www.first-blood.cn_bundle.crt;

	location / {
	    #获取ip的时候返回真实ip
	    proxy_set_header x-forwarded-for $remote_addr;  
		proxy_pass http://127.0.0.1:8080;
		proxy_connect_timeout 600;
		proxy_read_timeout 600;
	}
}
```
