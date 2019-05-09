## 扩展自定义函数

```javascript
$.fn.kevin = function() {
	console.log("a haha...");
}

$("a").kevin();
```

* 获取新对象

> 类似浅复制

```javascript
var a = {"id":1}
var b = {"name":"张三"};

console.log($.extend({},a,b));
```
