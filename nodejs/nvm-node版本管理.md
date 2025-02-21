# node多版本环境切换

> Node Version Manager（NVM） 是一种用于管理多个主动节点.js版本的工具

## node版本管理

* volta 管理node

* nvm

* asdf

### 安装指定版本

  

```sh
volta install node@16.18.0 

# 切换版本
volta pin node@16.20.0

# 设置镜像(nvm安装的node环境有时候无法下载依赖或卡住，配置下这个加速镜像)
npm config set registry https://registry.npmmirror.com

# 查看镜像源
npm config get registry
```

  

  ## 安装入门

[参考地址](https://blog.csdn.net/HuangsTing/article/details/113857145)

  ## 设置加速在命令行输入nvm root可以找到nvm安装路径 -> 找到 settings.txt 文件 -> 修改下载源

  ### 在文件中添加配置

  node_mirror: https://npmmirror.com/mirrors/node/

  npm_mirror: https://npmmirror.com/mirrors/npm/



  ```sh
  # 查看可用版本
  
  nvm list available
  
  # 安装指定版本
  
  nvm install 16.14.0
  
  # 使用指定版本
  
  nvm use 16.14.0
  
  # 查看安装版本
  
  nvm list
  ```

  
