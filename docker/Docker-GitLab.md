# GitLab

> 推荐内存：至少2GB

* 拉取最新镜像

```
docker pull gitlab/gitlab-ce
```

* 运行

```shell
docker run -d  -p 443:443 -p 80:80 -p 222:22 \
--name gitlab \
--privileged=true \
-e TZ=Asia/Shanghai \
-v /usr/local/docker/gitlab/config:/etc/gitlab \
-v /usr/local/docker/gitlab/logs:/var/log/gitlab \
-v /usr/local/docker/data:/var/opt/gitlab \
gitlab/gitlab-ce

# --privileged=true 防止权限不足
# -d：后台运行
# -p：将容器内部端口向外映射
# --name：命名容器名称
# --restart always 后台运行
# -v：将容器内数据文件夹或者日志、配置等文件夹挂载到宿主机指定目录

# 启用或停用开机自启
docker update --restart=always containerId/Name
docker update --restart=no containerId/Name

# 查看日志
docker logs gitlab
docker logs -f gitlab

# 更多参数的命令
docker run --name='gitlab' -d \
-v /home/git/gitlab_docker/data:/home/git/data \
-v /home/git/gitlab_docker/log:/var/log/gitlab \
-p 10022:22 -p 10080:80 \
-e 'GITLAB_PORT=10080' \
-e 'GITLAB_SSH_PORT=10022' \
-e 'DB_TYPE=postgres' -e 'DB_HOST=172.20.0.2' -e 'DB_PORT=5432' -e 'DB_USER=gitlab' -e 'DB_PASS=password' \
-e 'REDIS_HOST=172.20.0.5' \
-e 'GITLAB_EMAIL=zhonglong1988@hotmail.com' \
-e 'GITLAB_BACKUPS=weekly' \
-e 'GITLAB_HOST=120.78.137.250' \
-e 'GITLAB_SIGNUP=true' \
-e 'GITLAB_GRAVATAR_ENABLED=false' \
-e 'GITLAB_SECRETS_DB_KEY_BASE=pwgen -Bsv1 64' \
-e 'GITLAB_SECRETS_SECRET_KEY_BASE=pwgen -Bsv1 64' \
-e 'GITLAB_SECRETS_OTP_KEY_BASE=pwgen -Bsv1 64' \
--net docker_default \
sameersbn/gitlab
```

* 开放外部访问
  * 生成项目的URL访问地址是按容器的hostname来生成的，也就是容器的id。作为gitlab服务器，我们需要一个固定的URL访问地址，于是需要配置gitlab.rb

```shell
vim /usr/local/docker/gitlab/config/gitlab.rb


# 配置http协议所使用的访问地址,不加端口号默认为80
external_url 'http://192.168.56.101'

# 配置ssh协议所使用的访问地址和端口
gitlab_rails['gitlab_ssh_host'] = '1192.168.56.101'
gitlab_rails['gitlab_shell_ssh_port'] = 222 # 此端口是run时22端口映射的222端口

# 时区
gitlab_rails['time_zone'] = 'Asia/Shanghai'
:wq #保存配置文件并退出


# 重启docker
docker restart gitlab

# 配置时区可选操作，经测试没有任何区别
sudo gitlab-ctl reconfigure
sudo gitlab-ctl restart
sudo gitlab-ctl status
```

* 访问web

> 用户：`root`
>
> 默认密码：`cat /usr/local/docker/gitlab/config/initial_root_password`

* 修改密码

```shell
docker exec -it gitlab bash

gitlab-rails console -e production

# 获取用户
user = User.find_by(username: 'root')
user = User.where(id: 1).first
user = User.find_by(email: 'admin@example.com')

user.password='12345678'
user.password_confirmation='12345678'
user.save!

exit
```

> 也可以登录web管理，右上角：头像-preferences-Password进行修改

## 内存不够用？

> 解决方法: 将磁盘空间虚拟成内存使用

```shell
grep SwapTotal /proc/meminfo

cat /proc/swaps

dd if=/dev/zero of=/swapfile bs=1024 count=1024000

chmod 600 /swapfile
mkswap /swapfile

free -h

swapon /swapfile

swapon -s


# 禁用交换分区
swapoff /swapfile
rm -rf /swapfile
```

* 防止重启失效

```shell
vim /etc/fstab

# 在文件后面添加配置后重启
/swapfile swap swap defaults 0 0

# reboot重启
reboot
```



