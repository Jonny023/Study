* 映射`apk`下载

```bash
<!--进入配置文件编辑模式-->
vim conf/nginx.conf

<!--添加-->
location ~ .*\.(apk|ipa)$ {
    #指定存放路径：必须是相对路径
    root resources/app/; 
}
```
