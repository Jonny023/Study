## 监听各种事件

> `layui`的单选、复选、下拉等都需要用form监听才能生效

* `select`改变事件

```javascript
<select name="area" lay-filter="area" lay-verify="required"></select>

form.on('select(area)',function(data) {
    console.log(data.elem); // dom节点
});

```

* `select`输入框单击事件
```javascript
<select name="area" lay-filter="area" lay-verify="required"></select>

// 单击input输入框时触发
$('select[name="area"]').next().find('.layui-select-title input').click(function () {
    alert();
});

```
