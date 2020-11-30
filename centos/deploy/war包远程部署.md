

| 主机 | 192.168.0.22 |
| ---- | ------------ |
| 端口 | 22           |
| 密码 | 123456 |
| 用户 | root         |

* 备份脚本

```bash
#!/bin/bash
source /etc/profile
deploy_path="/usr/local/tomcat/apache-tomcat-9.0.39"
mv $deploy_path/webapps/gd.war $deploy_path/projec_bak/gd.war_`date +%Y%m%d_%H%M`.war
echo "备份完成";
```

| 备份、关停       | /usr/local/tomcat/apache-tomcat-9.0.39/shell/back.sh;/usr/local/tomcat/apache-tomcat-9.0.39/bin/shutdown.sh |
| ---------------- | ------------------------------------------------------------ |
| target directory | /usr/local/tomcat/apache-tomcat-9.0.39/webapps               |
| after deploy     | /usr/local/tomcat/apache-tomcat-9.0.39/bin/startup.sh        |
| commond          | clean install                                                |

