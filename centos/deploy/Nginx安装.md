### Nginx安装



#### 依赖环境安装

* 安装`gcc`

  ```bash
  yum -y install gcc
  ```

  

* 查看`gcc`版本

  ```bash
  gcc -v
  ```

  

* 安装`pcre`、`pcre-devel`

  ```bash
  yum install -y pcre pcre-devel
  ```



* 安装`zlib`

  ```bash
  yum install -y zlib zlib-devel
  ```



* 安装`openssl`

  ```bash
  yum install -y openssl openssl-devel
  ```

  

#### 安装nginx

* 下载安装包

  ```bash
  wget http://nginx.org/download/nginx-1.9.9.tar.gz  
  ```

* 解压

  ```bash
  tar -zxvf  nginx-1.9.9.tar.gz
  ```

* 进入`nginx`根目录执行安装

  ```bash
  ./configure
   
  make
   
  make install
  ```

* 切换目录到`/usr/local/nginx/conf`,修改配置文件`nginx.conf`，修改之前最好备份一下，主要是端口

  ```bash
  server {
      listen       8081;
      server_name  localhost;
  }
  ```

* 进入`/usr/local/nginx/sbin`下面

  ```bash
  cd /usr/local/nginx/sbin
  ```

* 启动`nginx`

  ```bash
  ./nginx
  ```

* 查看

  ```bash
  ps -ef | grep nginx
  ```



### 开机自启



* 进入目录

  ```bash
  cd /lib/systemd/system/
  ```

* 创建文件`vm nginx.service`

  ```bash
  [Unit]
  Description=nginx service
  After=network.target 
     
  [Service] 
  Type=forking 
  ExecStart=/usr/local/nginx/sbin/nginx
  ExecReload=/usr/local/nginx/sbin/nginx -s reload
  ExecStop=/usr/local/nginx/sbin/nginx -s quit
  PrivateTmp=true 
     
  [Install] 
  WantedBy=multi-user.target
  ```

  ```bash
  [Unit]:服务的说明
  Description:描述服务
  After:描述服务类别
  [Service]服务运行参数的设置
  Type=forking是后台运行的形式
  ExecStart为服务的具体运行命令
  ExecReload为重启命令
  ExecStop为停止命令
  PrivateTmp=True表示给服务分配独立的临时空间
  注意：[Service]的启动、重启、停止命令全部要求使用绝对路径
  [Install]运行级别下服务安装的相关设置，可设置为多用户，即系统运行级别为3
  ```

* 开机自启

  ```bash
  systemctl enable nginx
  ```

* 禁用开机自启

  ```bash
  systemctl disable nginx
  ```

  ```bash
  # systemctl start nginx.service　         启动nginx服务
  # systemctl stop nginx.service　          停止服务
  # systemctl restart nginx.service　       重新启动服务
  # systemctl list-units --type=service     查看所有已启动的服务
  # systemctl status nginx.service          查看服务当前状态
  # systemctl enable nginx.service          设置开机自启动
  # systemctl disable nginx.service         停止开机自启动
  ```

### Warning: nginx.service changed on disk. Run 'systemctl daemon-reload' to reload units.

* 运行

  ```bash
  systemctl daemon-reload
  ```

  



# 配置转发

```bash
location / {
    #root   html;
    #index  index.html index.htm;
    proxy_pass http://localhost:8088;
}

```



#### 创建软链接

```bash
sudo ln -s /usr/local/nginx/sbin/nginx /usr/bin/nginx
```

> 在任意目录下都可以使用nginx

```bash
sudo nginx  		         启动 

sudo nginx -s reload   配置文件变化后重新加载配置文件并重启nginx服务

sudo nginx -s stop     停止

sudo nginx -v  显示nginx的版本号
sudo nginx -V  显示nginx的版本号和编译信息
sudo nginx -t  检查nginx配置文件的正确性
sudo nginx -T  检查nginx配置文件的正确定及配置文件的详细配置内容
```



#### 开启`gzip`压缩功能

```bash
gzip  on;
gzip_buffers 32 4k;
gzip_comp_level 6;
gzip_min_length 100;
gzip_types text/css text/xml application/x-javascript;
```

* 重启`nginx

```bash
nginx -s reload
```





#### websocket连接不上

```bash
server {
        listen       8081;
        server_name  localhost;
		
		proxy_set_header Host $host:$server_port;
		proxy_set_header X-Real-IP $remote_addr;
		proxy_set_header REMOTE-HOST $remote_addr;
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		client_max_body_size 100m;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            #root   html;
            #index  index.html index.htm;
			proxy_pass http://localhost:8088;
			
			proxy_http_version 1.1;
			proxy_set_header Upgrade $http_upgrade;
			proxy_set_header Connection "upgrade";
			 
			proxy_connect_timeout 4s; 
			proxy_read_timeout 7200s; 
			proxy_send_timeout 12s; 
        }
        
}
```

