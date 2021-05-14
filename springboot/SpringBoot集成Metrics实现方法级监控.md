# `SpringBoot`集成`Metrics`实现方法级监控

[参考](https://www.codercto.com/a/11198.html)

* pom.xml依赖

```xml
<dependency>
    <groupId>com.codahale.metrics</groupId>
    <artifactId>metrics-core</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>com.codahale.metrics</groupId>
    <artifactId>metrics-servlets</artifactId>
    <version>3.0.2</version>
</dependency>
```

* 注册servlet，提供访问接口

```java
package com.example.demo.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册servlet和Metric实例
 */
@Configuration
public class MonitorConfig {

  @Bean
  public MetricRegistry metricRegistry() {
    return new MetricRegistry();
  }

  @Bean
  public ServletRegistrationBean servletRegistrationBean(MetricRegistry metricRegistry) {
    return new ServletRegistrationBean(new MetricsServlet(metricRegistry), "/monitor/metrics");
  }

}
```

* 定时窗口，打印控制台，若不需要可以在配置中配置禁用`monitor.report.console=false`

```java
package com.example.demo.config;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *  定义一个窗口每10秒输出一次到console
 */
@Configuration
@ConditionalOnProperty(prefix = "monitor.report", name = "console", havingValue = "true")
@Import(MonitorConfig.class)
public class MonitorReportConfig {

  @Bean
  public ConsoleReporter consoleReporter(MetricRegistry metrics) {
    ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build();
    reporter.start(10, TimeUnit.SECONDS);
    return reporter;
  }

}
```

* 配合监听

```java
package com.example.demo.monitor;

import com.codahale.metrics.MetricRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/**
 *  配置监听，监听方法或者类
 */
@Component
@Getter
@Slf4j
public class MethodMonitorCenter implements ApplicationContextAware {

  public static final String PACKAGE_NAME = "com.example.demo";

  @Autowired
  private MetricRegistry metricRegistry;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Map<String, Object> monitorBeans = new HashMap<>();
    monitorBeans.putAll(applicationContext.getBeansWithAnnotation(Controller.class));
    monitorBeans.putAll(applicationContext.getBeansWithAnnotation(Service.class));
    monitorBeans.putAll(applicationContext.getBeansWithAnnotation(RestController.class));

    log.info("monitor begin scan methods");
    monitorBeans.values().stream()
        .map(obj -> obj.getClass().getName())
        .map(this::trimString)
        .map(clzName -> {
          try {
            return Class.forName(clzName);
          } catch (Exception e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .filter(aClass -> aClass.getName().startsWith(PACKAGE_NAME))
        .forEach(this::getClzMethods);
  }

  private void getClzMethods(Class<?> clz) {
    Stream.of(clz.getDeclaredMethods())
        .filter(method -> method.getName().indexOf('$') < 0)
        .forEach(method -> {
          log.info("add method timer, method name: {}", method.toGenericString());
          metricRegistry.timer(method.toString());
        });
  }

  private String trimString(String clzName) {
    if (clzName.indexOf('$') < 0) return clzName;
    return clzName.substring(0, clzName.indexOf('$'));
  }

}
```

* 访问接口：http://localhost:8080/monitor/metrics

```java
{
    "version": "3.0.0",
    "gauges": {},
    "counters": {},
    "histograms": {},
    "meters": {},
    "timers": {
        "public java.lang.Object com.example.demo.controller.PostController.hello()": {
            "count": 7,
            "max": 1.3171504,
            "mean": 0.21167557142857144,
            "min": 0.019556300000000002,
            "p50": 0.0234397,
            "p75": 0.0448491,
            "p95": 1.3171504,
            "p98": 1.3171504,
            "p99": 1.3171504,
            "p999": 1.3171504,
            "stddev": 0.48754709105777577,
            "m15_rate": 0.006205939151789405,
            "m1_rate": 0.003972848064380463,
            "m5_rate": 0.011855262180984835,
            "mean_rate": 0.005962638149729374,
            "duration_units": "seconds",
            "rate_units": "calls/second"
        },
        "public org.springframework.http.ResponseEntity com.example.demo.controller.PostController.username()": {
            "count": 3,
            "max": 0.0077197,
            "mean": 0.0025953666666666667,
            "min": 0.0000323,
            "p50": 0.0000341,
            "p75": 0.0077197,
            "p95": 0.0077197,
            "p98": 0.0077197,
            "p99": 0.0077197,
            "p999": 0.0077197,
            "stddev": 0.004437802935387435,
            "m15_rate": 0,
            "m1_rate": 0,
            "m5_rate": 0,
            "mean_rate": 0.0025554116087842665,
            "duration_units": "seconds",
            "rate_units": "calls/second"
        }
    }
}
```

