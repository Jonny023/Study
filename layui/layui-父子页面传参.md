## layui多个页面（层级）传参


### 父页面向子页面传参

[参考地址](http://layer.layui.com/api.html#layer.getChildFrame)

* 父窗体

```javascript
layer.open({
  type: 2
  ,content: ''
  ,success: function(layero, index){
    var iframe = window['layui-layer-iframe'+index];
    //调用子页面的全局函数
    iframe.child("hello");
  }
})
```

* 子窗体

```javascript
<script>
    function child(obj) {
      console.log(obj);
    }
</script>
```

### 父窗口获取子页面回调方法

* 父窗体

```javascript
layer.open({
    type: 2,
    content: '/user/info',
    btn: function(index,layero) {
        var iframeWin1 = window[layero.find('iframe')[0]['name']];
        var iframeWin2 = window["layui-layer-iframe" + index];
        iframeWin1.getValue();
    }
});
```

* 子页面

```javascript
window.getValue = function () {
    alert(123456);
}
```
