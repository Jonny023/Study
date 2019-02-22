# Grails通过sessionId获取session对象

>  思路：自定义一个类用来监听`session`，所有`session`存入`map`中，`sessionId`作为读取的`key`

## 创建监听类 `SessionTracker`

```groovy
package com.session

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.web.context.WebApplicationContext

import javax.servlet.http.HttpSession
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class SessionTracker implements HttpSessionListener, ApplicationContextAware {

    private static final ConcurrentMap<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        def servletContext = ((WebApplicationContext) applicationContext).getServletContext()
        servletContext.addListener(this);
    }

    void sessionCreated(HttpSessionEvent httpSessionEvent) {
        sessions.putAt(httpSessionEvent.session.id, httpSessionEvent.session)
    }

    void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        sessions.remove(httpSessionEvent.session.id)
    }

    HttpSession getSessionById(id) {
        sessions.get(id)
    }
}
```

## 在 `grails-app/conf/resources.groovy` 中注册

```groovy
import com.session.SessionTracker

// Place your Spring DSL code here
beans = {

    // 自定义session监听器
    sessionTracker(SessionTracker)
}
```

## 获取`session`

```groovy
package com.genee

import org.springframework.web.context.request.RequestContextHolder

import javax.servlet.http.HttpSession

class HiController {

    // 注入监听对象
    def sessionTracker

    def index() {

        // 获取session
        def sessionId = RequestContextHolder.currentRequestAttributes().getSessionId()

        println "原sessionId:$sessionId"

        // 根据sessionId获取session对象
        HttpSession httpSession = sessionTracker.getSessionById(sessionId).getId()
        println "获取到session后:"+httpSession.getId()

        // 使session立即失效
        sessionTracker.getSessionById(sessionId).invalidate()
        render sessionId
    }
}
```

