# 页面a标签点击无效

```javascript
mui('body').on('tap','a',function(){document.location.href=this.href;});
```
