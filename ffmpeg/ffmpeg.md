# ffmpeg

[windows ffmpeg全量环境包](https://www.gyan.dev/ffmpeg/builds/ffmpeg-git-full.7z)

[转码](http://blog.chinaunix.net/uid-25885064-id-3396371.html)

[推流](https://www.jianshu.com/p/c141fc7881e7)

[视频教程](http://www.chungen90.com/?news_33/)

## nginx配置文件

> 1.`live`配置对应`ffmpeg`推流地址: rtmp://localhost:1935/live/xx
>
> 2.`hls`配置对应`ffmpeg`推流地址为: rtmp://localhost:1935/hls/xx，它对应的`http`地址为：http://localhost:80/hls/xx.m3u8

```properties
#user  nobody;
worker_processes  2;
 
events {
    worker_connections  8192;
}
 
rtmp {
    server {
        listen 1935;
        chunk_size 4000;
        application live {
             live on;
 
             # record first 1K of stream
             record all;
             record_path video;
             record_max_size 1K;
 
             # append current timestamp to each flv
             record_unique on;
 
            #  publish only from localhost
             #allow publish 127.0.0.1;
             allow publish all;
             #deny publish all;
 
             allow play all;
        }

        application hls {

            live on;
            hls on;  #开启hls
            hls_path C:/devtools/nginx/tmp/hls;
            hls_fragment 1s; #一个ts 文件的时长 5s
        }
    }
}
 
http {
    include       mime.types;
    default_type  application/octet-stream;
 
    sendfile        off;
    #tcp_nopush     on;
 
    server_names_hash_bucket_size 128;
 
    ## Start: Timeouts ##
    client_body_timeout   10;
    client_header_timeout 10;
    keepalive_timeout     30;
    send_timeout          10;
    keepalive_requests    10;
    ## End: Timeouts ##
 
    #gzip  on;
 
    server {
        listen       80;
        server_name  localhost;
 
 
        location /stat {
            rtmp_stat all;
            rtmp_stat_stylesheet stat.xsl;
        }
        location /stat.xsl {
            root nginx-rtmp-module/;
        }
        location /control {
            rtmp_control all;
        }

        location /hls {
            types {
                application/vnd.apple.mpegurl m3u8;
                video/mp2t ts;
            }
            root C:/devtools/nginx/tmp;
            add_header Access-Control-Allow-Origin *;
            add_header Cache-Control no-cache;
        }
 
        # For Naxsi remove the single # line for learn mode, or the ## lines for full WAF mode
        location / {
            root   html;
            index  index.html index.htm;
        }
 
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
  
    }
  
}
```

### 启动nginx

```shell
# 启动nginx
nginx.exe -c conf\nginx-win-rtmp.conf

# 停止nginx
nginx -s stop -c conf\nginx-win-rtmp.conf
```

* 查看支持编码

```shell
ffmpeg -codecs
```

## ffmpeg命令

### 查看设备

```shell
ffmpeg -list_devices true -f dshow -i dummy
```



#### windows检测摄像头是否可用

```shell
ffplay -f dshow -i video="USB 视频设备"  
# 或者
ffplay -f vfwcap -i 0
```

* 本地视频推流

```shell
ffmpeg.exe -re -i demo.wmv -f flv rtmp://127.0.0.1:1935/live/1
```

* 摄像头推流

```java
ffmpeg -f dshow -i video="USB 视频设备" -vcodec libx264 -preset:v ultrafast -tune:v zerolatency -f flv rtmp://127.0.0.1:1935/live/1
```

```shell
ffmpeg -f dshow -i video="USB 视频设备" -vcodec libx264 -preset:v ultrafast -tune:v zerolatency -f flv rtmp://127.0.0.1:1935/live/123

ffmpeg -f dshow -i video="USB 视频设备" -s 640x480 -vcodec libx264 -acodec copy -preset:v ultrafast -tune:v zerolatency -f flv rtmp://127.0.0.1:1935/live/1

# live
ffmpeg -r 30  -f dshow -i video="USB 视频设备" -vcodec h264 -max_delay 100 -f flv -g 5 -b 700000 rtmp://127.0.0.1:1935/live/1

# hls推流
ffmpeg -r 30  -f dshow -i video="USB 视频设备" -vcodec h264 -max_delay 100 -f flv -g 5 -b 700000 rtmp://127.0.0.1:1935/hls/1

# 基于地址
ffmpeg -r 30  -f dshow -i rtmp://ns8.indexforce.com/home/mystream -vcodec h264 -max_delay 100 -f flv -g 5 -b 700000 rtmp://127.0.0.1:1935/hls/88

ffmpeg -f dshow -i video="USB 视频设备" -vcodec libx264 -f flv rtmp://127.0.0.1:1935/live/88

ffmpeg -i rtmp://ns8.indexforce.com/home/mystream -vcodec libx264 -f flv rtmp://127.0.0.1:1935/live/88

ffmpeg -i rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov -vcodec libx264 -f flv rtmp://localhost/hls/88

ffmpeg -i rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov -vcodec libx264 -f flv rtmp://localhost/live/88

ffmpeg -f dshow -i video="USB 视频设备" -vcodec libx264 -f flv rtmp://localhost/hls/99
ffmpeg -f dshow -i video="USB 视频设备" -vcodec libx264 -f flv rtmp://localhost/live/99

ffmpeg -list_devices true -f dshow -i dummy  
ffmpeg -r 30 -f dshow -i video="USB 视频设备" -vcodec h264 -max_delay 100 -f flv -g 5 -b 700000 rtmp://localhost:1935/hls/1

ffmpeg -thread_queue_size 512 -r 30 -f dshow -i video="USB 视频设备" -vcodec h264 -max_delay 100 -f flv -g 5 -b 700000 rtmp://localhost:1935/hls/1
```

## 检测rtmp地址是否可用

```shell
ffprobe -v quiet -print_format json -show_streams rtmp://ns8.indexforce.com/home/mystream
```



## 在线测试地址（能用的rtmp源）

```
rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov
```

## ffmpeg实例参考

[参考](https://www.cxymm.net/article/qq_37059136/116737654)

