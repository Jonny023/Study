### 箭头函数

* 没有自己的`this` ，它的`this`始终指向让它生效的对象
* 没有`arguments`关键字

> 实例

```javascript
// 打印结果：3
let test1 = () => 1+2; 

// 执行1+1,无返回值
let test2 = () => { 1+1 }; 

// 返回{id:2,name:'张三'}
let test3 = () => ({id:2,name:'张三'})

let obj = {

	o1: function () {
		setTimeout(() => {console.log(this,0);});
	},

	o2: function () {
		setTimeout(function() { console.log(this,0);});
	}
}

// this指向obj
obj.o1();

// this指向Window
obj.o2();

// 运算
let add = (a,b,c) => (a+b*c)

// 结果为：7
add(1,2,3);
```
