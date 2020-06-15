* 命令行
```bash
$ git init    //git初始化
$ git remote add -f origin http://githhub/projectName.git   //添加远程仓库地址
$ git config core.sparsecheckout true    //开启sparse checkout功能
$ echo "fileName" >> .git/info/sparse-checkout   //fileName需要拉去的文件夹名称
$ cat .git/info/sparse-checkout   //查看配置文件信息
$ git pull origin master    //拉取远程哪个分支的文件目录，这里是master上的分支
```
