

## 安装docker-compose

```sh
# 下载docker-compose
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# 如果下载慢也可以手动下载，然后放到/usr/loca/bin/docker-compose目录
# 拿到地址到本地浏览器下载
echo "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)"

# 修改名字，名字改为docker-compose
mv docker-compose-Linux-x86_64 docker-compose

# 添加执行权限
sudo chmod +x /usr/local/bin/docker-compose

# 创建docker-compose软连接，任何位置可以运行docker-compose命令
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# 验证
docker-compose --version
```

