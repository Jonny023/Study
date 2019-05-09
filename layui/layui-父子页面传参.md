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
