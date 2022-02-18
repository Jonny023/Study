## nginx跨域配置

> 跨域核心配置：`add_header Access-Control-Allow-Origin *;`

```properties
server {

    listen       80;
    server_name  localhost;
    
    location / {
    	add_header Access-Control-Allow-Origin *;
    }
}
```

