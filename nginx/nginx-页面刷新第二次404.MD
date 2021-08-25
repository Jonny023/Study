# nginx 刷新页面404

> 如访问/hello.html的时候，$uri代表的就是hello.html

```groovy
location / {
    root html;
    index index.html index.htm;
    try_files $uri $uri/ /index.html;
}
```



