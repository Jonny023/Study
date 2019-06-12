
## mui页面无法滚动

* 代码如下
```javascript

<div class="mui-scroll-wrapper">

</div>

<script>

//先初始化一下
mui.init();

//再加入这段代码
(function($){
    $(".mui-scroll-wrapper").scroll({
        //bounce: false,//滚动条是否有弹力默认是true
        //indicators: false, //是否显示滚动条,默认是true
        deceleration: 0.0005 //flick 减速系数，系数越大，滚动速度越慢，滚动距离越小，默认值0.0006
    }); 
})(mui);
</script>
```

或

```html
<div class="mui-scroll-wrapper">
    <div class="mui-scroll">
        ...
    </div>
</div>
```
