# OpenJdk缺少字体包

> 解决方法

```dockerfile
FROM openjdk:8-jdk-alpine

RUN set -xe \
    && echo -e 'https://mirrors.aliyun.com/alpine/v3.6/main/\nhttps://mirrors.aliyun.com/alpine/v3.6/community/' > /etc/apk/repositories \
    && apk update \
    && apk upgrade \
    && apk --no-cache add ttf-dejavu fontconfig
```

