[插件地址](http://www.jq22.com/jquery-info15776)

> `html`代码

* `size`
  * `auto` - 自动(默认为纵向)
  * `portrait` - 纵向
  * `landscape` - 横向
* `margin: 0mm auto;` - 去掉页眉页脚

```html
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="./layui/css/layui.css" />
    <link rel="stylesheet" href="./print/files/comm.css" />
    
    <script type="text/javascript" src="./print/files/Print.js" ></script>

    <style>
    .main {
        width: 80%;
        margin: 15px auto;
    }
    </style>
    <style media="print">
    @page {
        size: landscape;  /* auto is the initial value */
        margin: 0mm auto; /* this affects the margin in the printer settings */
    }
    /*@page{*/
    /*    size:portrait;// 或landscape   设置横纵向打印*/
    /*    margin：0mm auto*/
    /*}*/
    </style>
</head>

<body>
<div class="layui-main" style="margin: 25px auto;">
    <a class="layui-btn" id="printBtn">打印</a>
</div>

<div class="main" id="main-data">
   <p>测试数据</p>
</div>

<script>
    document.getElementById('printBtn').onclick = function () {
        Print('#main-data', {
            onStart: function () {

            },
            onEnd: function () {

            }
        })
    }

</script>
</body>
</html>
```
