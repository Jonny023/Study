# nginx配置多个项目404

> 配置文件配置多个前端项目，访问出现404

### 原配置（不生效）

```nginx
location /pc/ {
      root   /data/html/pc/; 
      index  index.html index.html; 
}
location /h5/ {
   root /data/html/h5/;
   index  index.html index.htm; 
}
```



### 新配置（有效的）

#### 1.取消root目录重复值

```nginx
location /pc/ {
      root   /data/html/; 
      index  index.html index.html; 
}
```

#### 2.root 改成 alias

> location如果一个特定的url 要使用别名，不能用root，alias指定的目录是准确的，root是指定目录的上级目录，改动后即可以使用了【url和目录名不一样】

```nginx
location /pc/ {
  alias /data/html/pc/; 
  index  index.html index.htm; 
}
```

#### 3.配置多个server

