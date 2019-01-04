### 实际例子

* 一个ul列表设置了浮动，ul后面紧跟一个div容器，理想情况：`div排在ul后面，而不是ul并排`，此时设置div容器的css有时无效，那么这种情况就可以用清除浮动来实现
* 在`</ul>`后面的div上添加清除浮动

```html
/*清除浮动*/
.ul li {float:left;}

.box {clear:float;clear:both;}


<div class="main">
  <ul class="ul">
    <li>AAA</li>
    <li>BBB</li>
  </ul>
  <div class="box">BOX</div>
</div>

```
