
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
