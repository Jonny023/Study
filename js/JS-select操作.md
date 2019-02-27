* 获取select选中对象

```
var obj = document.getElementById('sel');
var elem = obj.options[obj.selectedIndex];
```

* 隐藏当前选中的option，显示之前隐藏的

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
    var ops = this.children,len = ops.length;
    for(var i=0;i<len;i++) {
      ops[i].style = "display:block";
    }
    var elem = this.options[this.selectedIndex];								
    elem.style = "display:none";

  };
</script>
```
