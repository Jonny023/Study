# 如何接入QQ登录

> 参照开发者指南[http://wiki.connect.qq.com/%E6%88%90%E4%B8%BA%E5%BC%80%E5%8F%91%E8%80%85](http://wiki.connect.qq.com/%E6%88%90%E4%B8%BA%E5%BC%80%E5%8F%91%E8%80%85)

### 1、登录[qq第三方平台](https://connect.qq.com/index.html)

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0325/872902fd03254716933a4ba5c0d0c144.png)

### 2、完善个人资料，提交审核

### 3、创建应用

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0325/7f3db090554e4cbda0572449f3ac5d8f.png)

### OAuth2.0流程

> QQ登录OAuth2.0总体处理流程如下：

* Step1：申请接入，获取appid和apikey；
* Step2：开发应用，并设置协作者帐号进行测试联调；
* Step3：放置QQ登录按钮；
* Step4：通过用户登录验证和授权，获取Access Token；
* Step5：通过Access Token获取用户的OpenID；
* Step6：调用OpenAPI，来请求访问或修改用户授权的资源。

[参考地址](http://wiki.connect.qq.com/%E5%87%86%E5%A4%87%E5%B7%A5%E4%BD%9C_oauth2-0)

### 4、下载[QQ SDK](http://wiki.open.qq.com/wiki/website/SDK%E4%B8%8B%E8%BD%BD)

* 若为`maven`或`gradle`，可以去maven仓库搜索`Sdk4J` 
* `sdk`提供一些比较方便的工具，如`HttpClient`，也可以自己封装`http工具`

> maven

```xml
<!-- https://mvnrepository.com/artifact/net.gplatform/Sdk4J -->
<dependency>
    <groupId>net.gplatform</groupId>
    <artifactId>Sdk4J</artifactId>
    <version>2.0</version>
</dependency>

```

> gradle

```groovy
// https://mvnrepository.com/artifact/net.gplatform/Sdk4J
compile group: 'net.gplatform', name: 'Sdk4J', version: '2.0'

```


---


### 5、下面讲一下大致流程

#### 第一步 获取Authorization Code

```
https://graph.qq.com/oauth2.0/authorize?response_type=code&g_ut=2&client_id=101481000&display=pc
&scope=get_user_info&state=123456&redirect_uri=https://www.first-blood.cn/app
```

#### 第二步 通过Authorization Code获取Access Token

```
https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&code=C8619228BD9123EF1A6874F891FC123
&client_id=101481000&client_secret=66825f34ee159f20572755a8fc1622d1&redirect_uri=https://www.first-blood.cn/app
```

#### 第三步 通过Access Token获取openid

```
https://graph.qq.com/oauth2.0/me?access_token=132A530A035CD5C71653291231320
```

* 返回数据`callback( {"client_id":"101484224","openid":"3E9E84C4ASADFSAGSDAF0DE8986D0D3"} );`

#### 第四步 获取用户信息

```
https://graph.qq.com/user/get_user_info?access_token=132A530A035CD5C716532916608AWEF&oauth_consumer_key=101481000
&openid=3E9E84C4ASADFSAGSDAF0DE8986D0D3
```
* 数据库保存`openid`，此`openid`是唯一的，而且是固定的，系统用户与之关联即可实现`qq登录`

### [查看更多参数详情](http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token)

