集成shiro重点：
```
//build.gradle中新增maven库
maven { url "https://dl.bintray.com/shiranr/plugins" }
maven { url "https://dl.bintray.com/animator013/plugins/" }

//dependencies中新增依赖：
compile 'org.grails.plugins:grails-shiro:3.0.1'
compile "org.freemarker:freemarker:latest.release"
compile group: 'net.mingsoft', name: 'shiro-freemarker-tags', version: '0.1'
```

通过命令创建域类：
```
shiro-quick-start --prefix=org.example.Shiro.
```
grails-app/conf目录下创建 application.properties文件，文件中写入：
```
## Freemarker 配置
spring.mvc.static-path-pattern=/assets/**
spring.freemarker.cache=false
spring.freemarker.content-type=text/html
spring.freemarker.expose-request-attributes=true
spring.freemarker.expose-session-attributes=true
spring.freemarker.charset=UTF-8
spring.freemarker.request-context-attribute=ctx
```
在src/main/groovy下面新建一个FreeMarkerConfigExtend类继承FreeMarkerConfigurer类覆盖afterPropertiesSet方法
```
package com.system

import com.jagregory.shiro.freemarker.ShiroTags
import freemarker.template.Configuration
import freemarker.template.TemplateException
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer

/**
 * 实现页面标签权限控制
 * @auther Lee
 * @Date 2018/4/19 15:44
 * return 
 *
 */
class FreeMarkerConfigExtend extends FreeMarkerConfigurer {
    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet()
        Configuration cfg = this.getConfiguration()
        cfg.setSharedVariable("shiro", new ShiroTags())//shiro标签
        cfg.setNumberFormat("#")//防止页面输出数字,变成2,000
        //可以添加很多自己的要传输到页面的[方法、对象、值]
        cfg.setDateFormat("yyyy-MM-dd")
        cfg.setTimeFormat("hh:mm")
        cfg.setDateTimeFormat("yyyy-MM-dd hh:mm")
        cfg.setBooleanFormat("true,false")
    }
}
```
接下来在grails-app/conf/spring/resources.groovy中注入
```
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver

// Place your Spring DSL code here
beans = {

    freeMarkerConfigurer(com.system.FreeMarkerConfigExtend) {
        templateLoaderPath = "\\"
    }
    viewResolver(FreeMarkerViewResolver) {
        cache = false
        prefix = "bin/freemarker"
        suffix = ".ftl"
        exposeSpringMacroHelpers = true
    }
}
```
freemarker模板文件为ftl后缀，在src/main目录下创建webapp,webapp中创建一个目录来存放ftl文件

### 控制器
```
package com.system

class IndexController {

    def index() {
        println "123456"
        render view: "/web/index"
    }
} 
```
控制器如何传值到ftl模板？
```
def obj = [id:1,name:'张三']
render view: '/web/index',model: [obj:obj]

//session传值
session.setAttr...(key,value)
```

页面如何取值？
```
${obj.id}
${obj.name}

## sesion传值
${Session['key']}
```



init下面BootStrap.groovy中创建用户
```
import com.system.ShiroPermission
import com.system.ShiroRole
import com.system.ShiroRolePermissionRel
import com.system.ShiroUser
import com.system.ShiroUserRoleRel
import org.apache.shiro.crypto.hash.Sha256Hash

class BootStrap {

    def init = { servletContext ->

        def exists = ShiroUser.findByUsername("admin")

        if(!exists) {

            def user1 = new ShiroUser("admin", new Sha256Hash("admin").toHex()).save(failOnError: true)
            def user2 = new ShiroUser("test", new Sha256Hash("test").toHex()).save(failOnError: true)
            def user3 = new ShiroUser("guest", new Sha256Hash("guest").toHex()).save(failOnError: true)

            def role1 = new ShiroRole("ROLE_ADMIN").save(failOnError: true)
            def role2 = new ShiroRole("ROLE_USER").save(failOnError: true)
            def role3 = new ShiroRole("ROLE_GUEST").save(failOnError: true)

            new ShiroUserRoleRel(user1, role1).save(failOnError: true)
            new ShiroUserRoleRel(user2, role2).save(failOnError: true)
            new ShiroUserRoleRel(user3, role3).save(failOnError: true)

            def p1 = new ShiroPermission("org.apache.shiro.authz.permission.WildcardPermission", "*").save(failOnError: true)

            new ShiroRolePermissionRel(role1, p1, "*:*", "*").save(failOnError: true)
            new ShiroRolePermissionRel(role2, p1, "test:index,create,show,edit,update", "*").save(failOnError: true)
            new ShiroRolePermissionRel(role3, p1, "*:*", "*").save(failOnError: true)

        }
        
    }

    def destroy = {
    }
}
```
### 注意：注释掉auth里面的注销方法里面的：webRequest.getCurrentRequest().session = null这句话，不然会报错

## 如何开放权限，比如某个功能无需登录即可访问，可以通过拦截器来控制，但是需要注意优先级，值越小优先级越高
```
package com.system


class IndexInterceptor {

    int order = HIGHEST_PRECEDENCE+100

    static anon = ["index"]

    IndexInterceptor() {
        match(controller: "index",action: "*")
    }

    boolean before() {

        //无需登录的权限
        if(actionName in anon) {
            println "success..."
            return true
        }else{
            accessControl(auth: true)
        }


    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
```

ftl模板文件中如何使用权限控制标签呢？
```
<#include "header.ftl"/>

    <h2>欢迎您！</h2>
    项目根路径：${ctx.contextPath!}

    <@shiro.guest>
        您当前是游客，<a href="${ctx.contextPath}/auth/login.html" class="dropdown-toggle qqlogin" >登录</a>
    </@shiro.guest>

    <br/>
    <@shiro.user>
        欢迎[<@shiro.principal/>]登录，<a href="${ctx.contextPath}/auth/signOut.html">退出</a>
    </@shiro.user>

    <br/>
    <@shiro.authenticated>
	    用户[<@shiro.principal/>]已身份验证通过
    </@shiro.authenticated>

    <br/>
    <@shiro.notAuthenticated>
        当前身份未认证（包括记住我登录的）
    </@shiro.notAuthenticated>

    <br/>
    <@shiro.hasRole name="ROLE_ADMIN">
	    用户[<@shiro.principal/>]拥有角色admin<br/>
    </@shiro.hasRole>

    <br/>
    <@shiro.hasAnyRoles name="admin,user,member">
        用户[<@shiro.principal/>]拥有角色admin或user或member<br/>
    </@shiro.hasAnyRoles>

    <br/>
    <@shiro.lacksRole name="ROLE_USER">
        用户[<@shiro.principal/>]不拥有admin角色
    </@shiro.lacksRole>

    <br/>
    <@shiro.hasPermission name="user:add">
        用户[<@shiro.principal/>]拥有user:add权限
    </@shiro.hasPermission>

    <br/>
    <@shiro.lacksPermission name="user:add">
        用户[<@shiro.principal/>]不拥有user:add权限
    </@shiro.lacksPermission>

    <#include "footer.ftl"/>
</body>
</html>
```
