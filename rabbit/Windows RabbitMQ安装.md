# Windows RabbitMQ安装



## 安装Erlang

* [Erlang云盘下载地址](https://pan.baidu.com/s/1c2826rA)

* [官网](http://www.erlang.org/downloads)

### 配置环境

* `ERLANG_HOME` - `D:\dev_tools\erl7.1\erts-7.1`
* `path` - `%ERLANG_HOME%`



## 安装RabbitMQ

* [RabbitMQ云盘下载地址](https://pan.baidu.com/s/1cqpG0u) 
* [官网](http://www.rabbitmq.com/download.html)

> 默认端口为`15672`

### 配置环境

* ### 激活 RabbitMQ's Management Plugin可视化

> 打开`dos`,输入`"‪D:\dev_tools\RabbitMQ Server\rabbitmq_server-3.6.5\sbin\rabbitmq-plugins.bat" enable rabbitmq_management`

* 完成提示

```
The following plugins have been enabled:
  mochiweb
  webmachine
  rabbitmq_web_dispatch
  amqp_client
  rabbitmq_management_agent
  rabbitmq_management

Applying plugin configuration to rabbit@DESKTOP-5J52E61... started 6 plugins.
```



* 管理员身份启动服务`net stop RabbitMQ && net start RabbitMQ`

* 创建用户，密码，绑定角色

  * 进入安装目录下的`sbin`

    ```bash
    cd /d D:\dev_tools\RabbitMQ Server\rabbitmq_server-3.6.5\sbin
    ```

* 查看已有用户及用户的角色：`rabbitmqctl.bat list_users`

* 新增用户：`rabbitmqctl.bat add_user username password`

* 再次查看用户列表

  ```bash
  D:\dev_tools\RabbitMQ Server\rabbitmq_server-3.6.5\sbin>rabbitmqctl.bat list_users
  Listing users ...
  rabbit  []
  guest   [administrator]
  ```

* 将用户`rabbit`授权为超级管理员

  ```
  rabbitmqctl.bat set_user_tags rabbit administrator
  ```

* 更改用户密码：`rabbitmqctl change_password userName newPassword`

* 删除用户：`rabbitmqctl.bat delete_user username`



## 访问地址：`http://localhost:15672/`



[参考地址](https://www.cnblogs.com/ericli-ericli/p/5902270.html)

