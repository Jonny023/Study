## 适用于

* `obj.click()`
* 行内`onclick`

> 代码如下

```html
<html>

	<head>
		<link rel="stylesheet" href="layui/css/layui.css"  media="all">
	</head>
	<body>
	
		<a class="layui-btn" id="test" href="javascript:;">测试</a><br/><br/>
		<a class="layui-btn" onclick="hello('this is a test')" href="javascript:;">hello</a>
		<script src="layui/layui.js"></script>
		<script type="text/javascript">
		
			layui.use(["jquery","layer","form"],function(){
				var $ = layui.$,layer = layui.layer,form = layui.form;
				$("#test").click(function(){
					hello("张三");
				});
				
				window.hello = function(obj) {
					layer.msg("hello world,"+obj);
				}
			});
			
		</script>		
		
	</body>
</html>
```

> 测试结果

* 点击`测试`按钮

  ![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2018/1122/8e5c184678894c8cba888ff9cfa78296.jpg)

* 点击`hello`按钮

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2018/1122/e449379bb8d44eb89df9928431c7b4ae.jpg)

