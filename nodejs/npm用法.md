## NODEJS安装及环境变量

* 官网
  + [nodejs官网](https://nodejs.org/en/)
  
  
* 安装完成，测试

```
node -v

npm -v
```

* 环境变量
  + 在nodejs目录下创建`node_global`及`node_cache`两个文件夹
  + 执行cmd命令
  
  ```
  npm config set prefix "C:\Program Files\nodejs\node_global"
  npm config set cache "C:\Program Files\nodejs\node_cache"
  ```

* 新建系统变量`NODE_PATH`,值`C\Program Files\nodejs\node_global\node_modules`

* 安装cnpm
  + 用淘宝镜像代替（速度快）[前往查看](https://npm.taobao.org/)
  + 执行cmd命令
  
  ```
  npm install -g cnpm --registry=https://registry.npm.taobao.org
  ```

* 验证cnpm

```
cnpm -v 
```

#### 注意
  + 若是提示没有版本，则需要将cnpm命令所在文件夹加入环境变量的path中
  + 如：:\Program Files\nodejs\node_global下，则将此路径追加到path中


### 常用命令

```
# install dependencies
npm install

# is error?
npm install webpack-dev-server -g

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report
```
