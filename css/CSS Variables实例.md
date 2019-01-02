## CSS动态传参
* 不兼容IE
* 必须以下划线`--`命名，如：`--demo-color`，`--demo--border`
* 大小写敏感

> 代码

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        #demo {
            width: 100%;
            background: var(--demo-color,red); /*默认为red*/
            width: var(--demo-width);
            color: var(--demo-c);
        }
    </style>
</head>
<body>

<div id="demo">
    hello,this is a test.
</div>

<br/>
<button onclick="change()">改变自己</button>

<script>
    window.onload = function (ev) {
        document.documentElement.style.setProperty('--demo-color','orange');
        document.documentElement.style.setProperty('--demo-width','200px');
        document.documentElement.style.setProperty('--demo-c','#FFF');
    }
    
    function change() {
        let colors = ["green","blue","orange","red","black"];
        let i = Math.ceil(Math.random()*4)
        document.documentElement.style.setProperty('--demo-color',colors[i]);
        document.documentElement.style.setProperty('--demo-width','300px');
        document.documentElement.style.setProperty('--demo-c','#FFF');
    }
</script>
</body>
</html>
```

> 效果

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0102/845f64d9156846de92966f47144dce98.gif)

