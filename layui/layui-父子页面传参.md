## layui多个页面（层级）传参


### 父页面向子页面传参

* 父窗体

```javascript
layer.open({
  title: this.text,
  type: 2,
  skin: 'layui-layer-rim', //加上边框
  area: [window.screen.width / 2 + 'px', window.screen.height / 2 + 'px'], //宽高
  maxmin: true, //开启最大化最小化按钮
  content: "taskDetail.html",
  success: function (layero, index) {
      // 获取子页面的iframe
      var iframe = window['layui-layer-iframe' + index];
      // 向子页面的全局函数child传参
      iframe.child("hello");
  }
});
```

* 子窗体

```javascript
<script>
    function child(obj) {
      console.log(obj);
    }
</script>
```
