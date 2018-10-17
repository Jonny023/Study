* 注入服务

```
def springSecurityService

```

* 获取登录用户

```
def username = springSecurityService?.principal?.username

```
* 获取登录用户角色(集合)

```
def roles = springSecurityService.getPrincipal().getAuthorities() 
//or springSecurityService.authentication.authorities //or springSecurityService?.principal?.authorities

```

* 清理requestmap缓存

```
springSecurityService.clearCachedRequestmaps()

```

* gsp页面标签

```
//未登录
<sec:ifNotLoggedIn>
    登录
</sec:ifNotLoggedIn>

//已登录
<sec:ifLoggedIn>
    <sec:username/>
</sec:ifLoggedIn>

<!--同时匹配 -->
<sec:ifAllGranted roles="ROLE_ADMIN,ROLE_USER">
    ...
</sec:ifAllGranted>

<!--匹配任意一个 -->
<sec:ifAnyGranted roles='ROLE_ADMIN,ROLE_USER'>
    ...
</sec:ifAnyGranted>

<!--匹配非ROLE_USER角色 -->
<sec:ifNotGranted roles="ROLE_USER">
    ...
</sec:ifNotGranted>

<!--获取当前登录用户 -->
<sec:loggedInUserInfo field="username"/>

<!--匹配指定角色 -->
<sec:access expression="hasRole('ROLE_USER')">
    I'm a user.
</sec:access>

<!--匹配指定请求 -->
<sec:access url="/admin/user">
    The requestURL is "/admin/user"
</sec:access>

```

* 只允许账号为admin的访问

```
authentication.name=='admin'
```

* 表达式对照

| 标识 | 表达式 |
| ------ | ------ |
| ROLE_ADMIN | hasRole('ROLE_ADMIN') |
| ROLE_USER,ROLE_ADMIN | hasAnyRole('ROLE_USER','ROLE_ADMIN') |
| ROLE_ADMIN,IS_AUTHENTICATED_FULLY | hasRole('ROLE_ADMIN') and isFullyAuthenticated() |
| IS_AUTHENTICATED_ANONYMOUSLY | permitAll |
| IS_AUTHENTICATED_REMEMBERED | isAuthenticated() or isRememberMe() |
| IS_AUTHENTICATED_FULLY | isFullyAuthenticated() |

### 几种配置方式

* 默认配置Annotation

```
grails.plugin.springsecurity.securityConfigType = "Annotation"
```

```
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],

	[pattern: '/api/test/**', access: ['permitAll']]
]
```

* interceptUrlMap方式

```
grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"
```

```
grails.plugin.springsecurity.interceptUrlMap = [
   '/secure/someAction':      ["hasRole('ROLE_ADMIN')"],
   '/secure/someOtherAction': ["authentication.name == 'admin'"]
]

grails.plugin.springsecurity.interceptUrlMap = [
   '/':                ['permitAll'],
   '/index':           ['permitAll'],
   '/index.gsp':       ['permitAll'],
   '/assets/**':       ['permitAll'],
   '/**/js/**':        ['permitAll'],
   '/**/css/**':       ['permitAll'],
   '/**/images/**':    ['permitAll'],
   '/**/favicon.ico':  ['permitAll'],
   '/login/**':        ['permitAll'],
   '/logout/**':       ['permitAll'],
   '/secure/**':       ['ROLE_ADMIN'],
   '/finance/**':      ['ROLE_FINANCE', 'isFullyAuthenticated()'],
]
```

* Requestmap方式（保存在数据库中）

```
grails.plugin.springsecurity.securityConfigType = "Requestmap"
```

```
for (String url in [
      '/', '/index', '/index.gsp', '/**/favicon.ico',
      '/assets/**', '/**/js/**', '/**/css/**', '/**/images/**',
      '/login', '/login.*', '/login/*',
      '/logout', '/logout.*', '/logout/*']) {
   new Requestmap(url: url, configAttribute: 'permitAll').save()
}
new Requestmap(url: '/profile/**',    configAttribute: 'ROLE_USER').save()
new Requestmap(url: '/admin/**',      configAttribute: 'ROLE_ADMIN').save()
new Requestmap(url: '/admin/role/**', configAttribute: 'ROLE_SUPERVISOR').save()
new Requestmap(url: '/admin/user/**', configAttribute: 'ROLE_ADMIN,ROLE_SUPERVISOR').save()
new Requestmap(url: '/j_spring_security_switch_user',
               configAttribute: 'ROLE_SWITCH_USER,isFullyAuthenticated()').save()
```
