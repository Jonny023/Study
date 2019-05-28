## 页面传递

* 子页面模拟父页面点击事件
  * touuchstart必须，不然只会触发一次

```javascript
document.querySelector("#job").addEventListener("tap", function (evt) {
   var d = window.parent.document.querySelector("#hyqz");
    mui.trigger(d, 'touchstart');
    mui.trigger(d, 'tap');
});
```
