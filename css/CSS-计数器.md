# 实现效果

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0102/76c1f3839977455aa3f91726faa95e33.png)

* 代码

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        body {
            counter-reset: icecream;
        }
        input:checked {
            counter-increment: icecream;
        }
        .total {
            color: red;
            font-weight: bold;
        }
        .total::after {
            content: counter(icecream);
        }
    </style>
</head>
<body>

<h2>今天吃什么？</h2>
<input type="checkbox" value="宫保鸡丁"/>宫保鸡丁<br/>
<input type="checkbox" value="青椒肉丝"/>青椒肉丝<br/>
<input type="checkbox" value="土豆肉丝"/>土豆肉丝<br/>
<input type="checkbox" value="红烧排骨"/>红烧排骨<br/>
<input type="checkbox" value="可乐鸡翅"/>可乐鸡翅<br/>
<input type="checkbox" value="干煸四季豆"/>干煸四季豆<br/>
<h3>共选择了<span class="total"></span>个菜。</h3>
<br/>

</body>
</html>
```

参考 [张鑫旭博客](https://www.zhangxinxu.com/wordpress/2014/12/css-counters-pseudo-class-checked-numbers/)
