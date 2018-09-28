* 获取select选中对象

```
var obj = document.getElementById('sel');
var elem = obj.options[obj.selectedIndex];
```

* 隐藏选中的option

```
<select id="sel">
  <option value=''>请选择...</option>
  <option>Java</option>
  <option>Python</option>
  <option>C++</option>
  <option>C#</option>
</select>

<script>
  document.getElementById('sel').onchange=function(){
    var elem = this.options[this.selectedIndex];
    console.log(elem);
    if(elem.value !=''){
      elem.style = "display:none";
    }

  };
</script>
```
