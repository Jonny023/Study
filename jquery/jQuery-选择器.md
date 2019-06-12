## 选择器分类
* `ID`选择器
* `CLASS`选择器（类选择器）
* 元素选择器

> ID选择器

```javascript
$("#user").val();
```

> class选择器

```javascript
$(".user")
```

> 元素选择器

```javascript
$("p")
```

> 查找不包含某个class、id的元素

```javascript
$("div").not(".old")
$("div").not("#user")
```
