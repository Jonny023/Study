# Centos命令

## 指定时间段日志

```sh
cat xxx.log |sed -n '/2020-08-19 23:12/,/2020-08-19 23:15/p'
sed -n '/2022-03-18 15:55/,/2022-03-18 16:55/p' stat.log

sed -n '/2022-03-18 15/,/2022-03-18 16/p' stat.log
cat stat.log |sed -n '/2022-03-18 15/,/2022-03-18 16/p'
```



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

### 查看指定文件大小

> 查询包含green关键字文件大小

```sh
ls | grep green | xargs -i du -h {}
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

## 磁盘占用空间

### 系统

```sh
lsblk
```

### docker 

```sh
docker system df
```

