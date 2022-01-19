# Centos命令

## 远程登陆

```bash
ssh user@ip

ssh root@192.168.1.2
```

## 修改主机名

```shell
hostnamectl set-hostname vm1

# 重启
reboot

# 或者
shutdown -r now
```

## 查看端口

### netstat

```shell
netstat -tunlp | grep 8080
```

### ss

```shell
ss -tunlp | grep 8080
```

### lsof

> 第三方组件，需要安装：yum install -y lsof

```shell
lsof -i:8080
```

