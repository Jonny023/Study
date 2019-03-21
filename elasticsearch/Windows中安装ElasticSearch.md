# Windows中安装ElasticSearch 

[参考文档](https://blog.csdn.net/qq_40454655/article/details/79291106)

[下载地址](https://www.elastic.co/cn/downloads/elasticsearch#ga-release)

* 解压到本地，运行`bin/elasticsearch.bat`，访问`http://localhost:9200/`,出现如下所示结果：

  ```json
  {
    "name" : "Hs1hdWf",
    "cluster_name" : "elasticsearch",
    "cluster_uuid" : "gQfDdrlKTK-2ltAHK_Q2Tg",
    "version" : {
      "number" : "6.6.2",
      "build_flavor" : "default",
      "build_type" : "zip",
      "build_hash" : "3bd3e59",
      "build_date" : "2019-03-06T15:16:26.864148Z",
      "build_snapshot" : false,
      "lucene_version" : "7.6.0",
      "minimum_wire_compatibility_version" : "5.6.0",
      "minimum_index_compatibility_version" : "5.0.0"
    },
    "tagline" : "You Know, for Search"
  }
  ```



* 安装`nodejs`(此处省略)

* 修改`elasticsearch.yml`文件,保存并重启`elasticsearch.bat`

  ```yaml
  http.cors.enabled: true 
  http.cors.allow-origin: "*"
  node.master: true
  node.data: true
  
  # 原配置文件此部分为注释的，可自行新增或修改
  network.host: 0.0.0.0
  http.port: 9200
  cluster.name: my-es
  ```

* 安装`head`

  * [下载](https://github.com/mobz/elasticsearch-head )

* 解压到本地，修改`D:\dev_tools\elasticsearch-head-master\Gruntfile.js`,添加`hostname: '*',`

  ```js
  connect: {
      server: {
          options: {
              hostname: '*',
              port: 9100,
              base: '.',
              keepalive: true
          }
      }
  }
  ```

* 进入`D:\dev_tools\elasticsearch-head-master\_site`,修改app.js 中下文内容为[服务器](https://www.baidu.com/s?wd=%E6%9C%8D%E5%8A%A1%E5%99%A8&tn=24004469_oem_dg&rsv_dl=gh_pl_sl_csd)地址，如果是本机部署不修改也可以

  ```js
  init: function(parent) {
  			this._super();
  			this.prefs = services.Preferences.instance();
  			this.base_uri = this.config.base_uri || this.prefs.get("app-base_uri") || "http://localhost:9200";
  			if( this.base_uri.charAt( this.base_uri.length - 1 ) !== "/" ) {
  				// XHR request fails if the URL is not ending with a "/"
  				this.base_uri += "/";
  			}
  ```

* 进入项目主目录，执行`npm install`

* 启动项目`npm run start`,若安装了`grunt`，可以使用`grunt server `启动，若没安装，执行`npm install -g grunt-cli`安装

## 最后访问[localhost:9100](localhost:9100)

