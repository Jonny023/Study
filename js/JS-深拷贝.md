> 通过`Object.assign()`深拷贝
* 拷贝数组的时候需要注意
  * 如：`Object.assign([1,2],[11])`，他是将前一个对应位置的值用后面的覆盖

```javascript
var obj = {id: 1, age: 20};
var obj_bak = Object.assign({},obj)；
obj_back.id = 20;
// 输出{id: 20, age: 20}
```

> 通过`JSON.parse(JSON.stringify())`

```javascript
var obj = {id: 1, age: 20};
var obj_bak = JSON.parse(JSON.stringify(obj));
// obj_bak为{id: 1, age: 20}
```

### 注意
* 不能过`=`直接赋值

> 例如

```javascript
var m = {score: 80,name: "语文"}；
var n = m; // 赋值后不不修改是没问题的

// 修改n的值
n.name = "数学";

// console.log(m); 此时m的值也被修改,这不是我们想要的结果
```
