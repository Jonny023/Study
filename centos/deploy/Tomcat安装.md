### tomcat安装

[下载地址](http://mirror.bit.edu.cn/apache/tomcat)

* 命令下载

  ```bash
  wget http://mirror.bit.edu.cn/apache/tomcat/tomcat-9/v9.0.37/bin/apache-tomcat-9.0.37.tar.gz
  ```

  > 也可以下载后自行上传

* 解压

  ```java
  tar -zxvf apache-tomcat-9.0.37.tar.gz
  ```

* 修改配置：`tomcat/bin/catalina.sh`

  ```bash
  JAVA_OPTS="-Xms512m -Xmx1024m -Xss1024K -XX:PermSize=512m -XX:MaxPermSize=1024m"
  export TOMCAT_HOME=/usr/local/env/tomcat/apache-tomcat-9.0.37
  export CATALINA_HOME=/usr/local/env/tomcat/apache-tomcat-9.0.37
  export JRE_HOME=/usr/local/env/java/jdk1.8.0_161/jre
  export JAVA_HOME=/usr/local/env/java/jdk1.8.0_161
  ```

* 启动`tomcat`

  ```bash
  cd tomcat/bin
  ./startup.sh
  ```

* 测试

  ```bash
  curl localhost:8080
  ```

  

### 服务器停用

* 查看端口

  ```bash
  ps -ef | grep tomcat
  
  root     12851     1  2 15:33 pts/0    00:00:05 /usr/local/env/java/jdk1.8.0_161/jre/bin/java -Djava.util.logging.config.file=/usr/local/env/tomcat/apache-tomcat-9.0.37/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Xms512m -Xmx1024m -Xss1024K -XX:PermSize=512m -XX:MaxPermSize=1024m -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catalina.webresources -Dorg.apache.catalina.security.SecurityListener.UMASK=0027 -Dignore.endorsed.dirs= -classpath /usr/local/env/tomcat/apache-tomcat-9.0.37/bin/bootstrap.jar:/usr/local/env/tomcat/apache-tomcat-9.0.37/bin/tomcat-juli.jar -Dcatalina.base=/usr/local/env/tomcat/apache-tomcat-9.0.37 -Dcatalina.home=/usr/local/env/tomcat/apache-tomcat-9.0.37 -Djava.io.tmpdir=/usr/local/env/tomcat/apache-tomcat-9.0.37/temp org.apache.catalina.startup.Bootstrap start
  root     13213  8586  0 15:37 pts/0    00:00:00 grep --color=auto tomcat
  ```

  > `12851`就是进程`pid`

* 结束进程

  ```bash
  kill -9 12851
  ```

### 查看tomcat实时日志

* `tail`命令查看最新的`200`行

  ```bash
  tail -200f tomcat/logs/catalina.out
  ```



---



# 另一种查看占用端口

* 需要安装`lsof`

  ```bash
  yum install lsof
  ```

* 查看指定端口是否被占用

  ```bashj
  lsof -i:8080
  ```



---





### 开机自启

* 在`tomcat/bin`目录下创建`setenv.sh`脚本

  ```bash
  # set JAVA_HOME
  export JAVA_HOME=/usr/local/env/java/jdk1.8.0_161
  export JRE_HOME=$JAVA_HOME/jre
  export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
  
  export CATALINA_HOME=/usr/local/env/tomcat/apache-tomcat-9.0.37
  export CATALINA_BASE=/usr/local/env/tomcat/apache-tomcat-9.0.37
  # set tomcat pid
  CATALINA_PID="$CATALINA_BASE/tomcat.pid"
  ```

  

* 创建`tomcat.service`到`/usr/lib/systemd/system`目录下`vi /usr/lib/systemd/system/tomcat.service`

  ```bash
  [Unit]
  Description=Tomcat
  After=syslog.target network.target remote-fs.target nss-lookup.target
   
  [Service]
  Type=forking
  Environment="JAVA_HOME=/usr/local/env/java/jdk1.8.0_161"
  PIDFile=/usr/local/env/tomcat/apache-tomcat-9.0.37/tomcat.pid
  ExecStart=/usr/local/env/tomcat/apache-tomcat-9.0.37/bin/startup.sh
  ExecReload=/bin/kill -s HUP $MAINPID
  ExecStop=/bin/kill -s QUIT $MAINPID
  PrivateTmp=true
   
  [Install]
  WantedBy=multi-user.target
  ```

  

* 启动服务

  ```bash
  配置开机启动 
  
  systemctl enable tomcat
  
  启动tomcat
  systemctl start tomcat
  停止tomcat
  systemctl stop tomcat
  重启tomcat
  systemctl restart tomcat
  
  因为配置pid，在启动的时候会再tomcat根目录生成tomcat.pid文件，停止之后删除。
  
  同时tomcat在启动时候，执行start不会启动两个tomcat，保证始终只有一个tomcat服务在运行。
  
  多个tomcat可以配置在多个目录下，互不影响。
  tomcat1.service tomcat2.service
  
  重新加载配置
  systemctl daemon-reload
  ```

  

