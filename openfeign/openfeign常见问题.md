## openfeign常见问题

1.启动报错

> The bean 'demo.FeignClientSpecification' could not be registered. A bean with that name has already been defined and overriding is disabled.

* 可以指定contextId解决这个问题
	* `@FeignClient(name = "demo", configuration = FeignConfig.class, contextId = "xxx")`
	* 使用spring.main.allow-bean-definition-overriding=true配置，允许bean覆盖
	* 只保留一个@FeignClient的name相同的放到同一个接口里面
