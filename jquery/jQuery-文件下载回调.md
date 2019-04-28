# jQuery.fileDownload插件

* 原理

> 前台通过定时判断cookie是否与后台返回的cookie一致

## 实现代码

* 后台添加`cookie`

```java
response.setHeader("Set-Cookie", "fileDownload=true; path=/");
```

* 前台

```javascript
<!-- 需要依赖jquery-->
<script src="https://cdn.bootcss.com/jquery.fileDownload/1.4.2/jquery.fileDownload.min.js"></script>

var url = "${request.contextPath}/down?type=pdf";
$.fileDownload(url,{
    httpMethod: 'POST',
    // data:$("#form").serialize(),
    prepareCallback:function(url){
        layer.load(0);
    },
    successCallback:function(url){
        layer.closeAll();
        layer.msg('下载成功！');
    },
    failCallback: function (html, url) {
        layer.closeAll();
        layer.msg("网络异常");
       
    }
});
```
