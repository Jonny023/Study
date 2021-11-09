# docker安装Rabbitmq

## 拉取镜像

```shell
docker pull rabbitmq
```



## 创建数据目录
```shell
mkdir -p /usr/local/rabbitmq/data
mkdir -p /usr/local/rabbitmq/conf
mkdir -p /usr/local/rabbitmq/log
```

## 授权
```shell
chmod -R 777 /usr/local/rabbitmq 
```

## 运行容器命令
```shell
docker run -d --name rabbitmq --network host -v /usr/local/rabbitmq/data:/var/lib/rabbitmq -v /usr/local/rabbitmq/conf:/etc/rabbitmq -v /usr/local/rabbitmq/log:/var/log/rabbitmq rabbitmq
```

## 开启控制台管理
```shell
docker exec -it rabbitmq bash
rabbitmq-plugins enable rabbitmq_management
```

## 创建用户 
```shell
rabbitmqctl add_user admin admin
```

##  给用户授权角色 
```shell
rabbitmqctl set_user_tags admin administrator
```

## 给用户添加权限 
```shell
rabbitmqctl set_permissions -p / admin "." "." ".*"
```

## 端口

> 默认用户名和密码为`guest`，不支持远程连接

```shell
15672 web访问端口
5672 客户端端口
```

