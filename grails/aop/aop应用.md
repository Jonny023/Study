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

## 二

```groovy
    xmlns aop:"http://www.springframework.org/schema/aop"
    auditInterceptor(AuditInterceptor)  //This is the sample Interceptor I did 
    aop.config("proxy-target-class":true) {}




import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
class AuditInterceptor {
    

    @Before("execution(* com.crowdnetic.maven.factory.*.*(..))")
    public void test() {
//        String methodName = point.getSignature().getName();
//        List<Object> args = Arrays.asList(point.getArgs());
        System.out.println("调用前连接点方法为：Mongodb");
    }

    @Before("execution(* com.crowdnetic.maven.pos2.merchant.AppliedMerchantDao.insert(..))")
    public void daoTest(JoinPoint point) {
        String methodName = point.getSignature().getName();
        List<Object> args = Arrays.asList(point.getArgs());
        System.out.println("调用前连接点方法为：AppliedMerchantDao"+ methodName + ",参数为：" + args);
    }


    @Before("execution(* com.crowdnetic.maven.pos2.merchant.GPosM_OnBoardingController.*(..))")
    public void controllerTest() {
//        String methodName = point.getSignature().getName();
//        List<Object> args = Arrays.asList(point.getArgs());
        System.out.println("调用前连接点方法为：GPosM_OnBoardingController");
    }

}

```
