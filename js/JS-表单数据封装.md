# 表单数据直接封装为对象

```javascript
var data={};
var formDatas = new FormData(document.querySelector("form"));
formDatas.forEach(function(v, k) {
    data[k] = v;
});
```
