# Grails REST项目



* 引入依赖

```
compile "org.grails.plugins:spring-security-rest:2.0.0.M1"
```

* 新建`CorsFilter`跨域过滤器

```groovy
package com.troutbird.pharmatics.cors

import org.springframework.web.filter.OncePerRequestFilter

import javax.annotation.Priority
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Jonny
 */

@Priority(Integer.MIN_VALUE)
class CorsFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        String origin = req.getHeader("Origin");

        boolean options = "OPTIONS".equals(req.getMethod());
        if (options) {
            if (origin == null) return;
            resp.addHeader("Access-Control-Allow-Headers", "origin, authorization, accept, content-type, x-requested-with");
            resp.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
            resp.addHeader("Access-Control-Max-Age", "3600");
        }

        resp.addHeader("Access-Control-Allow-Origin", origin == null ? "*" : origin);
        resp.addHeader("Access-Control-Allow-Credentials", "true");

        if (!options) chain.doFilter(req, resp);
    }
}
```

* `grails-app/conf/spring/resources.groovy`中注册`bean`

```groovy
beans = {
    corsFilter(CorsFilter)
}
```

* `application.groovy`配置

```groovy
// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'demo.auth.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'demo.auth.UserRole'
grails.plugin.springsecurity.authority.className = 'demo.auth.Role'
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
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
//	[pattern: '/assets/**',      filters: 'none'],
//	[pattern: '/**/js/**',       filters: 'none'],
//	[pattern: '/**/css/**',      filters: 'none'],
//	[pattern: '/**/images/**',   filters: 'none'],
//	[pattern: '/**/favicon.ico', filters: 'none'],
//	[pattern: '/**',             filters: 'JOINED_FILTERS'],
    //Stateless chain
    [pattern: '/api/**',
            filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'],

    //Traditional chain
    [pattern: '/**', filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter']

]

grails.plugin.springsecurity.rest.token.storage.useMemcached = true
```

* `urlMapping.groovy`配置

```groovy
"/api/books"(resources: 'book')
```

