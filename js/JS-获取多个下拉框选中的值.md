## HTML

```html
<select>
    <option selected>1</option>
    <option>2</option>
    <option>3</option>
</select>
<select>
    <option>1</option>
    <option selected>2</option>
    <option>3</option>
</select>
<select>
    <option>1</option>
    <option>2</option>
    <option selected>3</option>
</select>
```

## JS

```javascript
<script>
    var elems=document.querySelectorAll("select");
    elems.forEach(function(elem){
        console.log(elem.value);
    });
</script>
```

## ES6

```javascript
let value = [...document.querySelectorAll("select")].map(item => {return item.value});
console.log(value);
```
