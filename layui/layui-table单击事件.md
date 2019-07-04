# 单击行选中

> 点击或双击行时触发。该事件为 `layui 2.4.0` 开始新增

* html

```html
<table id="table_demo" lay-filter="retirement"></table>

```

* js

```javascript

table.on('checkbox(retirement)', function (obj) {
    if (obj.checked === true) {
        $(".layui-table-body tr").addClass('layui-table-click');
    } else {
        $(".layui-table-body tr").removeClass('layui-table-click');
    }
});

// 监听行点击
table.on('row(retirement)', function (obj) {
    // obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');//选中行样式
    var index = obj.tr.data('index');//当前行
    if (obj.tr.hasClass("layui-table-click") === true) {
        obj.tr.removeClass('layui-table-click');
        obj.tr.find("input[type=checkbox]").prop("checked", false);
        table.cache["table_demo"][index].LAY_CHECKED = false;// 修改缓存区选中状态
        form.render("checkbox");
        return;
    }
    obj.tr.addClass('layui-table-click');
    obj.tr.find("input[type=checkbox]").prop("checked", true);
    table.cache["table_demo"][index].LAY_CHECKED = true; // 修改缓存区选中状态
    form.render("checkbox");
});
```

### 表格中有工具条的需要特殊处理，不然点击按钮也会触发选中

```javascript
//监听工具条
table.on('tool(demo)', function(obj){
    layui.stope(obj);
}
```
