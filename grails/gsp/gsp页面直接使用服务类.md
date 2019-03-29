> 如何在gsp页面注入服务

```groovy
<%
    def testService = grailsApplication.classLoader.loadClass('com.test.TestService').newInstance()
%>
${testService.hello()}
```

> 静态文件标签，可引入css/js/img
* `src` - 指定文件名，可加路径，如:`login/logo.png`
* `absolute` - 值为`true`或`false`,默认为`false`，若为`true`，地址则为:`http:localhost:8080/assets/login/logo.png`

```jsp
"${g.assetPath(src: "default.png",absolute:true)}"
```
