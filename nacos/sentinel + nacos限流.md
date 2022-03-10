# sentinel + nacos限流

nacos添加配置

```json
[
	{
		"resource":"test限流",
		"limitApp":"default",
		"grade": 1,
		"count": 1,
		"strategy": 1,
		"controlBehavior": 0,
		"clusterMode": false
	}
]
```

* `resource`：资源名，即限流规则的作用对象
* `limitApp`：流控针对的调用来源，若为 default 则不区分调用来源
* `grade`：限流阈值类型（QPS 或并发线程数）；
  * `0`代表根据并发数量来限流，
  * `1`代表根据QPS来进行流量
* `count`：限流阈值
* `strategy`：调用关系限流策略
* `controlBehavior`：流量控制效果（直接拒绝、Warm Up、匀速排队）
* `clusterMode`：是否为集群模式

