```xml
<?xml version="1.0" encoding="gbk"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p" xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-4.1.xsd
		http://www.springframework.org/schema/mvc
		http://www.springframework.org/schema/mvc/spring-mvc-4.1.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
        <property name="dataSource" ref="dataSource" />
        <property name="transactionManager" ref="transactionManager" />
        <property name="databaseSchema" value="ACT"/>
        <property name="databaseSchemaUpdate" value="true" />
        <property name="jobExecutorActivate" value="false" />
    </bean>

    <bean id="objectMapper" class="com.fasterxml.jackson.databind.ObjectMapper"/>

    <bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
        <property name="processEngineConfiguration" ref="processEngineConfiguration" />
    </bean>

    <bean id="repositoryService" factory-bean="processEngine"
          factory-method="getRepositoryService" />
    <bean id="runtimeService" factory-bean="processEngine"
          factory-method="getRuntimeService" />
    <bean id="taskService" factory-bean="processEngine"
          factory-method="getTaskService" />
    <bean id="historyService" factory-bean="processEngine"
          factory-method="getHistoryService" />
    <bean id="managementService" factory-bean="processEngine"
          factory-method="getManagementService" />
    <bean id="identityService" factory-bean="processEngine"
          factory-method="getIdentityService" />

    <!--加入Spring Activiti-Modeler的运行配置 -->
    <context:component-scan base-package="org.activiti.rest.editor" />
    <context:component-scan base-package="org.activiti.rest.diagram" />


    <!-- AOP 日志配置 -->
    <!--<aop:aspectj-autoproxy proxy-target-class="true" />-->

    <!--&lt;!&ndash;<bean id="logService" class="com.rxzy.ebms.process.OperationLogController" />&ndash;&gt;-->
    <!--<bean id="logService" class="com.rxzy.ebms.log.OperationLogService" />-->
    <!--<aop:config proxy-target-class="true">-->
        <!--<aop:aspect ref="logService">-->
            <!--<aop:pointcut id="performance" expression="execution(* com.rxzy.ebms..*(..))"/>-->
            <!--<aop:before pointcut-ref="performance" method="takeSeats"/>-->
            <!--<aop:before pointcut-ref="performance" method="turnOffCellPhones"/>-->
            <!--<aop:after-returning pointcut-ref="performance" method="applaud"/>-->
            <!--<aop:after-throwing pointcut-ref="performance" method="demandRefund"/>-->
        <!--</aop:aspect>-->
    <!--</aop:config>-->

</beans>

```
