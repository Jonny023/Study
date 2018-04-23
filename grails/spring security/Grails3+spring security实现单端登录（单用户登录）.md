新建项目
2、打开根目录下的build.gradle文件，dependencies中添加spring-security依赖
```
compile 'org.grails.plugins:spring-security-core:3.1.2'
```
3、创建用户、角色的domain，用命令快速生成域类：
```
grails s2-quickstart com.system UserInfo RoleInfo
```
4、自定义一个ConcurrentSingleSessionAuthenticationStrategy类实现SessionAuthenticationStrategy接口覆盖默认方法
```
package com.session

import org.springframework.security.core.Authentication
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.util.Assert

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 会话管理类
 */
class ConcurrentSingleSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    private SessionRegistry sessionRegistry

    /**
     * @param 将新的会话赋值给sessionRegistry
     */
    public ConcurrentSingleSessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
        Assert.notNull(sessionRegistry, "SessionRegistry cannot be null")
        this.sessionRegistry = sessionRegistry
    }
    /**
     * 覆盖父类的onAuthentication方法
     * 用新的session替换就的session
     */
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

        def sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false)
        def principals = sessionRegistry.getAllPrincipals()
        sessions.each {
            if (it.principal == authentication.getPrincipal()) {
                it.expireNow()
            }
        }


    }
}
```
5、打开grails-app/conf/spring/resource.groovy，配置DSL
```
import com.session.ConcurrentSingleSessionAuthenticationStrategy
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy
import org.springframework.security.web.session.ConcurrentSessionFilter

// Place your Spring DSL code here
beans = {

    sessionRegistry(SessionRegistryImpl)
    //很重要
    sessionFixationProtectionStrategy(SessionFixationProtectionStrategy){
        migrateSessionAttributes = true
        alwaysCreateSession = true
    }
    // "/login/already"为重定向请求
    concurrentSingleSessionAuthenticationStrategy(ConcurrentSingleSessionAuthenticationStrategy,ref('sessionRegistry'))
    registerSessionAuthenticationStrategy(RegisterSessionAuthenticationStrategy,ref('sessionRegistry'))
    sessionAuthenticationStrategy(CompositeSessionAuthenticationStrategy,[ref('concurrentSingleSessionAuthenticationStrategy'), ref('sessionFixationProtectionStrategy'), ref('registerSessionAuthenticationStrategy')])
    concurrentSessionFilter(ConcurrentSessionFilter, ref('sessionRegistry'), "/login/already")
}
```
6、在grails-app/conf目录下application.groovy类中添加
```
//grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
//grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/your-url' //登录成功后跳转地址

grails.plugin.springsecurity.filterChain.filterNames = [ 'securityContextPersistenceFilter', 'logoutFilter', 'concurrentSessionFilter', 'rememberMeAuthenticationFilter', 'anonymousAuthenticationFilter', 'exceptionTranslationFilter', 'filterInvocationInterceptor' ]
```
