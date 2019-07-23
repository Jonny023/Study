## `Nginx`配置本地目录预览

### 配置

```
server{
   listen 80;
   server_name static.javaweb.io;
   location / {
       root /usr/local/static;        
       autoindex on;                       # 开启索引    
       charset utf-8,gbk;                  # 解决文件名称中文乱码的问题
       autoindex_exact_size on;            # 显示文件大小        
       autoindex_localtime on;             # 显示最后修改时间     
   }
}
```
