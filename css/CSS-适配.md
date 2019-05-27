# 移动端适配

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
