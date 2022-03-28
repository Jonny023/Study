# docker安装sftp服务

## 拉取镜像

```sh
# 搜索
docker search sftp

# 拉取镜像
docker pull atmoz/sftp
```

## 启动容器

```sh
# 单个用户启动
docker run --name sftp -v /data/docker/volumes/sftp/upload:/home/admin/upload --privileged=true -p 2022:22 -d atmoz/sftp admin:pass:1001


# 多用户
docker run --name sftp -v /data/docker/volumes/sftp/conf/users.conf:/etc/sftp/users.conf:ro -v /data/docker/volumes/sftp/data:/home --privileged=true -p 2022:22 -d atmoz/sftp
```

## 自定义用户

```sh
# 添加用户配置
vi /data/docker/volumes/sftp/conf/users.conf

#user:pass:uid:gid　用户名:密码:用户id:组id
# 用户信息
admin:123456:1001:100
test:123456:1002:100

# 修改配置文件权限
chmod 755 /data/docker/volumes/sftp/conf/users.conf
```

## 权限问题

> 通过xftp连接sftp上传文件提示：`Sending the file failed.`

```sh
# 容器启动后，用户会在数据目录下生成对应的目录：/data/docker/volumes/sftp/data/admin
# 但这个/data/docker/volumes/sftp/data/admin目录并不能直接上传文件，需要将目录权限修改为777
# 需要在这个目录下单独创建一个目录并修改权限才能上传文件
mkdir -p /data/docker/volumes/sftp/data/admin/upload
chmod 777 /data/docker/volumes/sftp/data/admin/upload

# 重启容器
docker restart sftp
```

