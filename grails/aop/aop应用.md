# aop的应用

```groovy
class MyService{
    @AuditLog
    def method1() {
        println "method1 called"
        method2()
    }
    @AuditLog
    def method2() {
        println "method2 called"
    }
}

class AuditLogInterceptor implements MethodInterceptor {
    @Override
    Object invoke(MethodInvocation methodInvocation) throws Throwable {
        println "${methodInvocation.method}"
        return methodInvocation.proceed();
    }
}

aop {
    config("proxy-target-class": true) {
        pointcut(id: "auditLogInterceptorPointcut", expression: "@annotation(xxx.log.AuditLog)")
        advisor('pointcut-ref': "auditLogInterceptorPointcut", 'advice-ref': "auditLogInterceptor")
    }
}

auditLogInterceptor(AuditLogInterceptor) {}

class MyService{
    static transactional = false
    def grailsApplication

    @AuditLog
    def method1() {
        println "method1 called"
        grailsApplication.mainContext.myService.method2()
        //method2()
    }
    @AuditLog
    def method2() {
        println "method2 called"
    }
}
```
