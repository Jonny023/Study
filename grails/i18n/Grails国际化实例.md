# Grails实现国际化（中英文切换）

* 在`grails-app/i18n`分别配置英文（`messages.properties`），中文：（`messages_zh_CN.properties`）

> 英文配置`messages.properties`

```properties
user.username=Username
user.password=Password
username.tip=Enter username
password.tip=Enter password
```

> 中文配置`messages_zh_CN.properties`

```properties
user.username=用户名
user.password=密码
username.tip=请输入用户名
password.tip=请输入密码
```

> `gsp`页面代码

```html
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>
    <div class="main">
        <form action="${createLink(controller: "lang",action: "index")}" autocomplete="off">
            <table>
                <tr>
                    <td colspan="2">
                        <a href="${request.contextPath}/lang/index/zh_CN" class="btn btn-danger">中文</a>
                        <a href="${request.contextPath}/lang/index/en_US" class="btn btn-info">英文</a>
                    </td>
                </tr>
                <tr>
                    <td class="col-lg-1">
                        <g:message code="user.username" />
                    </td>
                    <td class="col-lg-11">
                        <input type="text" name="username" placeholder="<g:message code="username.tip"/>" class="form-control">
                    </td>
                </tr>
                <tr>
                    <td>
                        <g:message code="user.password" />
                    </td>
                    <td>
                        <input type="password" name="username" placeholder="<g:message code="password.tip"/>" class="form-control">
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="button" class="btn btn-default" value="登录">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
```

> 控制器

```groovy
package i18n

import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.support.RequestContextUtils

class LangController {

    def index() {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request)
        if("zh_CN".equals(params.id)) {
            localeResolver.setLocale(request, response, new Locale("zh", "CN"))
        } else {
            localeResolver.setLocale(request, response, new Locale("en", "US"))
        }
        redirect(uri:"/")
    }
}
```

> 实现效果

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0104/77808c7a11c846c594741f0de0718db6.gif)
