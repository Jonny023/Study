# Alibaba cloud toolkit远程部署

* idea安装`Alibaba cloud toolkit`插件

* 选择`deploy to Host`设置主机、端口及密码

  | Target Directory      | /usr/local/project                    |
  | --------------------- | ------------------------------------- |
  | after deploy[Commond] | cd /usr/local/project;./bin/start.sh; |
  | after deploy[Commond] | /usr/local/project/bin/start.sh;      |
  | before lanuch         | maven项目：模块 ：clean install       |
  | before lanuch         | gradle项目：Run Gradle task           |

* 创建脚本

```bash
cd /usr/local/project/         ##进入工作目录
mkdir bin                      ##创建脚本保存目录
vi start.sh                    ##创建脚本
```

* 启动执行脚本

```bash
#!/bin/bash
deploy_path="/usr/local/project"
#这里可替换为你自己的执行程序
APP_NAME=chat.jar

#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh 脚本名.sh [start|stop|restart|status]"
    exit 1
}

#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
  #如果不存在返回1，存在返回0
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

#启动方法
start(){
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is already running. pid=${pid} ."
  else
	nohup java -jar $deploy_path/$APP_NAME --spring.profiles.active=dev > $deploy_path/log/log.txt 2>&1 &
    echo "${APP_NAME} start success"
  fi
}

#停止方法
stop(){
  is_exist
  if [ $? -eq "0" ]; then
    kill -9 $pid
  else
    echo "${APP_NAME} is not running"
  fi
}

#输出运行状态
status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is running. Pid is ${pid}"
  else
    echo "${APP_NAME} is NOT running."
  fi
}

#重启
restart(){
  stop
  start
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
```

* 备份脚本

```bash
#!/bin/bash
source /etc/profile
deploy_path="/usr/local/project"
mv $deploy_path/chat.jar $deploy_path/history/chat.jar_`date +%Y%m%d_%H%M`.jar
echo "备份完成";
```

