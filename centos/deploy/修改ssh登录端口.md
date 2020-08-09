### 修改ssh登录端口

* 修改配置

  ```bash
  vi /etc/ssh/sshd_config
  # 把默认的端口
  Port 10086
  ```

* 添加防火墙规则

  ```bash
  firewall-cmd --permanent --add-port=10086/tcp
  ```

* 启动防火墙

  ```bash
  systemctl start firewalld
  ```

  > 提示`Unit is masked`, 执行：`systemctl unmask firewalld.service`

* 启用规则

  ```bash
  firewall-cmd --reload
  ```

* 重启`sshd`服务

  ```bash
  systemctl status sshd.service
  
  systemctl restart sshd.service
  ```

  