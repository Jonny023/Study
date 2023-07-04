## NODEJS安装及环境变量

* 官网
  + [nodejs官网](https://nodejs.org/en/)
  
  
* 安装完成，在dos中分别输入如下指令测试

> 如果是解压版，直接在path路径下添加环境变量: `C:\Program Files\nodejs`

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

* 新建系统变量`NODE_PATH`,值`C:\Program Files\nodejs\node_global\node_modules`

### 安装cnpm
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
  + 如：c:\Program Files\nodejs\node_global下，则将此路径追加到path中
  + npm root -g 查看global配置


### 常用命令

```
# 安装依赖
npm install

# 出现错误?
npm install webpack-dev-server -g

# 开发环境热部署 localhost:8080
npm run dev

# 生产环境编译打包
npm run build

# 生产环境编译打包并且生成报告
npm run build --report

npm config set registry https://registry.npm.taobao.org
```
