# 移动端适配

* 根据不同客户端设置meta
```javascript
if(/Android (\d+\.\d+)/.test(navigator.userAgent)){
    var version = parseFloat(RegExp.$1);
    if(version>2.3){
        var phoneScale = parseInt(window.screen.width)/640;
        if(/MZ-M571C/.test(navigator.userAgent)){
            document.write('<meta name="viewport" content="width=640, minimum-scale = 0.5, maximum-scale= 0.5">');
        }else if(/M571C/.test(navigator.userAgent)&&/LizhiFM/.test(navigator.userAgent)){
            document.write('<meta name="viewport" content="width=640, minimum-scale = 0.5, maximum-scale= 0.5">');
        }else{
            document.write('<meta name="viewport" content="width=640, minimum-scale = '+ phoneScale +', maximum-scale = '+ phoneScale +', target-densitydpi=device-dpi">');
        }
    }else{
        document.write('<meta name="viewport" content="width=640, target-densitydpi=device-dpi">');
    }
}else{
    document.write('<meta name="viewport" content="width=640, user-scalable=no, target-densitydpi=device-dpi">');
}

```

* 自动调整容器宽度

```javascript
<script>
    var width = $(window).width();
    var px2rem = $('#content *');
    for (var i = 0; i < px2rem.length; i++) {
        var element = px2rem[i];
        var widthPx = element.style.width;
        if (!widthPx) continue;
        var widthSize = widthPx.substr(0, widthPx.length - 2);
        if(widthSize > width) {
            element.style.width = (width / 15) + 'rem';
        }
    }
</script>
```

# [更多详情](https://juejin.im/entry/59ca3c6df265da064f2024af)
