如何关闭父级弹出层窗口：
```javascript
var index = parent.layer.getFrameIndex(window.name);  
parent.layer.close(index); 
```
## layui下拉选择框name属性不能用某些关键字，不然渲染不出来，name使用county可以测试一下

layui忽略下拉框美化
```html
在select加上：lay-ignore

<select lay-ignore>
  <option>…</option>
</select>
```
