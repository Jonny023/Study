# Dom操作

* 删除指定节点

```javascript
function removeElement(_element){
   var _parentElement = _element.parentNode;
   if(_parentElement){
          _parentElement.removeChild(_element);
   }
}
```
