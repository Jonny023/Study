# iperf2

* `iperf -s & iperf -s -u`在docker-compose和k8s中依然适用，方式2太麻烦

> iperf3能一个端口同时支持tcp和udp，但是不支持并发
>
> iperf2支持并发，但是默认是单协议运行

## 方式1

> docker run时指定sh -c "iperf -s & iperf -s -u" 一个前台一个udp后台运行，这样就能同时支持tcp和udp了

```sh
docker run -itd --name iperf2 --hostname iperf2 \
  -p 5001:5001 \
  -p 5001:5001/udp \
  visago/iperf2:latest \
  sh -c "iperf -s & iperf -s -u"
```

## 方式2

> 直接将运行命令构建到容器里面，运行时无需指定

```sh
docker run -itd --name iperf2 --hostname iperf2  visago/iperf2:latest iperf -s
# 查看版本
docker run --rm --name iperf2_tmp --hostname iperf2  visago/iperf2:latest iperf -v 
# 查看
docker logs -f iperf2


docker pull visago/iperf2:latest --platform=arm64
docker pull visago/iperf2:latest --platform=amd64

#Dockerfile
FROM visago/iperf2:latest

ENTRYPOINT ["/bin/sh","-c"]
CMD ["iperf -s & iperf -s -u"]


docker build --no-cache -t iperf2:v2.1.4 .

docker run -itd --name iperf2 --hostname iperf2 \
  -p 5001:5001 \
  -p 5001:5001/udp \
  iperf2:v2.1.4
  
# 测试tcp
docker run --rm --network host --entrypoint iperf iperf2:v2.1.4 -c 192.168.56.101 -p 5001

# 测试udp
docker run --rm --network host --entrypoint iperf iperf2:v2.1.4 -c 192.168.56.101 -u -b 100M
```

## 方式3

> k8s配置

```
containers:
- name: iperf2-server
  image: visago/iperf2:latest
  command:
    - sh
    - -c
    - |
      iperf -s &
      iperf -s -u
```

