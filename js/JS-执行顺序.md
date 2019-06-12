#### 分别创建两个`js`和两个`html`

* `one.js`

```javascript
function test() {
	alert("function1");
}
```

* `two.js`

```javascript
function test() {
	alert("function2");
}
```

* `index1.html`

```html
<!DOCTYPE html>
<html lang="en">
	<body>
		<a onclick="test()">提交</a>
		<script src="./one.js"></script>
		<script src="./two.js"></script>
		
	</body>
</html>
```

* `index2.html`

```html
<!DOCTYPE html>
<html lang="en">
	<body>
		<a onclick="test()">提交</a>
		<script  defer="defer" src="./one.js"></script>
		<script src="./two.js"></script>
		
	</body>
</html>
```

* 运行`index1.html`

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0612/8659c918eb3f4b09bf9c9c2faa9cefe1.png)

* 运行`index2.html`

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0612/810a0954f5e44bf4945ec2117c192c70.png)

## 总结

* `js`默认加载方式为同步加载， 页面点击事件调用多个函数为最后加载的那个

* `html5`的`async`可异步加载

* `html5`全面支持的`defer`和`async`都会立即执行当前`js`脚本，阻塞后面的脚本

* `async`加载顺序是乱序的，并行执行（异步），跟引入顺序无关（哪个加载完成就调用哪个）

* `defer`加载也是并行的，但初始化完成之后是顺序执行的，如前面的`index2.html`中引入的两个`js`脚本都有`defer`属性
那么加载完成之后，点击时会弹出`function2`，两个`js`有相同函数，想要始终执行某一个`js`脚本中的函数，可以添加`defer`
属性，页面执行完成始终执行有`defer`属性的`js`脚本

## [参考](https://www.jianshu.com/p/3aa3a3e27417)
