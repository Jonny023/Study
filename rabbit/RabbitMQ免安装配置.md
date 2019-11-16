# RabbitMQ免安装配置

* 下载[**erlang**](<https://www.erlang.org/downloads>)

* 配置`erlang`环境变量，添加`C:\Program Files\erl10.5\erts-10.5\bin`到`path`路径下
* 测试: 运行`cmd`进入`dos`，输入`erl`或者`erl -version`显示相关信息表示成功

```
C:\Users\Administrator>erl
Eshell V10.5  (abort with ^G)
1>
C:\Users\Administrator>erl -version
Erlang (SMP,ASYNC_THREADS) (BEAM) emulator version 10.5
```



* 下载[**RabbitMQ**](https://github.com/rabbitmq/rabbitmq-server/releases)压缩包并解压到本地

* 配置环境变量

  * `RABBIT_SERVER  D:\dev_tools\rabbitmq_server-3.7.21\sbin`
  * 将`%RABBIT_SERVER%`添加到`path`下

  

* ### 激活rabbitmq_management

  * 进入`cmd`输入`rabbitmq-plugins.bat enable rabbitmq_management`

  

* ### 开启服务

  * `rabbitmq-server start`
  * 默认端口`15672`

* ### 访问地址

  * [http://localhost:15672](http://localhost:15672)

  * 默认用户名、密码：`guest/guest`

  

# 用户管理

* **查看已有用户及角色**
  * `rabbitmqctl.bat list_users`

* **新增用户**
  * `rabbitmqctl.bat add_user username password`

* **授权为超级管理员**
  * `rabbitmqctl.bat set_user_tags username administrator`

* **更改用户密码**
  * `rabbitmqctl change_password userName newPassword`
* **删除用户**
  * `rabbitmqctl.bat delete_user username`



### 注意

* `client`端通信口：`5672`
* `web`管理口：`15672`
* `server`间内部通信口：`25672`
* `erlang`发现口：`4369`