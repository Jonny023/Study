## dataset属性

> 作用：获取以“data-”开头的属性对象

* html代码

```html
<p data-name="张三" data-age="20" data-sex="男">Hello World!</p>
```

* javascript

```javascript
let elem = document.querySelector("p");
console.log(elem.dataset);

//result
{name:"张三",age:20,sex:"男"}
```
