> html

```html
<select onchange="change(this)">
  <option>语文</option>
  <option>数学</option>
  <option>英语</option>
</select>
```

> JS

```javascript
function change(obj) {
  console.log(obj.options.selectedIndex);
  console.log(obj.options[obj.options.selectedIndex].innerHTML);
  console.log(obj.options[obj.options.selectedIndex].value);

}
```
