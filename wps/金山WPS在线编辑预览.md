# 金山WPS在线编辑预览

[开放平台](https://open.wps.cn/)

#### 文档地址

[doc](https://open.wps.cn/docs/wwo/access/api-list#des2)

# 目录

[1. 用户会话的身份校验](https://open.wps.cn/docs/wwo/access/api-list#des1)
[2. 获取文件元数据](https://open.wps.cn/docs/wwo/access/api-list#des2)
[3. 获取用户信息](https://open.wps.cn/docs/wwo/access/api-list#des3)
[4. 通知此文件目前有哪些人正在协作](https://open.wps.cn/docs/wwo/access/api-list#des4)
[5. 上传文件新版本](https://open.wps.cn/docs/wwo/access/api-list#des5)
[6. 获取特定版本的文件信息](https://open.wps.cn/docs/wwo/access/api-list#des6)
[7. 文件重命名](https://open.wps.cn/docs/wwo/access/api-list#des7)
[8. 获取所有历史版本文件信息](https://open.wps.cn/docs/wwo/access/api-list#des8)
[9. 新建文件](https://open.wps.cn/docs/wwo/access/api-list#des9)
[10. 回调通知](https://open.wps.cn/docs/wwo/access/api-list#des10)

#### Java服务端源码

* [gitee](https://gitee.com/mose-x/wps-view-java.git)

### 用户会话的身份校验

```javascript
//请求地址
http://192.168.0.66/v1/3rd/user/info

//请求参数：application/json，ids的值为数据库表w_user_t用户表的user_id
{
	"ids": [
		"1"
	],
	"_w_signature": "tBnhFqvVrC1LcJKye1m7GjzvqoA%3D"
}

//返回数据
{
	"msg": "ok",
	"code": 200,
	"users": [
		{
			"id": "1",
			"name": "张三",
			"avatar_url": "http://123hyh.free.idcfengye.com/导图.png"
		}
	],
	"status": "success"
}
```



### 获取文件元数据

```javascript
// _w_filetype:两种db和web
http://192.168.0.66/v1/3rd/file/info?_w_appid=1764349b86fc41ae8ef5dbbedc498aeb&_w_signature=tBnhFqvVrC1LcJKye1m7GjzvqoA=&_w_userid=1&_w_filetype=web&_w_filepath=http://docs.hk.utools.club/staff.xls

//返回
{
	"msg": "ok",
	"code": 200,
	"file": {
		"name": "staff.xls",
		"version": 1,
		"size": 26624,
		"creator": "-1",
		"create_time": 1606960152941,
		"modify_time": 0,
		"download_url": "http://docs.hk.utools.club/staff.xls",
		"user_acl": {
			"rename": 0,
			"history": 0
		},
		"watermark": {
			"type": 0,
			"value": "",
			"fillstyle": "rgba( 192, 192, 192, 0.6 )",
			"font": "bold 20px Serif",
			"rotate": 0,
			"horizontal": 50,
			"vertical": 50
		}
	},
	"user": {
		"id": "2",
		"name": "我",
		"permission": "read",
		"avatar_url": "https://zmfiletest.oss-cn-hangzhou.aliyuncs.com/user0.png"
	},
	"status": "success"
}
```



### 获取Web预览地址

```json
http://192.168.0.66/v1/api/file/getViewUrlWebPath?fileUrl=http://docs.hk.utools.club/staff.xls

//返回数据
{
	"msg": "ok",
	"code": 200,
	"data": {
		"expires_in": 600,
		"token": "fd771275951b44e2bc864aef145f9389",
		"wpsUrl": "https://wwo.wps.cn/office/s/fd771275951b44e2bc864aef145f9389?_w_userid=-1&_w_filetype=web&_w_filepath=http://docs.hk.utools.club/staff.xls&_w_tokentype=1&_w_appid=1764349b86fc41ae8ef5dbbedc498aeb&_w_signature=v%2BB%2B9PZb8dEpJ2N7zD9IPI5Efhc%3D"
	},
	"status": "success"
}
```

> 前端直接访问预览地址`wpsUrl`





### 获取Db预览地址

```json
http://192.168.0.66/v1/api/file/getViewUrlDbPath?fileId=2&userId=1

//返回数据
{
	"msg": "ok",
	"code": 200,
	"data": {
		"expires_in": 600,
		"token": "92238ad28b634c1f80bc10a922144ebf",
		"wpsUrl": "https://wwo.wps.cn/office/s/2?_w_userid=1&_w_filetype=db&_w_filepath=staff.xls&_w_tokentype=1&_w_appid=1764349b86fc41ae8ef5dbbedc498aeb&_w_signature=ILJ8G9hIZ5zFmDVWFnak37LSB4o%3D"
	},
	"status": "success"
}
```

> 前端直接访问预览地址`wpsUrl`



### 预览、编辑必须的几个接口

```js
# 获取文件元数据
/v1/3rd/file/info

# 回调通知
#v1/3rd/onnotify

# 上传新版本
/v1/3rd/file/save

# 获取用户信息
/v1/3rd/user/info

# 获取文件编辑地址
/v1/api/file/getViewUrlDbPath?fileId=3&userId=5
```

> 特别注意：

* 返回数据结构必须和官方示例一致
* 文件大小必须相差不大
* 文件版本最小为1，且必须设置
* download_url必须和官网配置的回调地址一致
* 回调地址设置：管理中心 - 金山文档在线编辑 - 进入服务 - 管理 - 数据回调地址

