> 如何在gsp页面注入服务

```groovy
<%
    def testService = grailsApplication.classLoader.loadClass('com.test.TestService').newInstance()
%>
${testService.hello()}
```
