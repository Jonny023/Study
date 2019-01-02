* 面包屑实例

  * `inline`兼容IE7
  * `inline-block`最低只能兼容至IE8
  
> 代码

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>测试面包屑</title>

    <style>
        .content {
            border-bottom: 2px solid #ccccff;
        }

        .h2 {
            margin-bottom: -2px; /* 使得两条线重叠 */
            border-bottom: 2px solid #ff7800;
            display: inline; /* inline和inline-block效果相同，inline-block不兼容IE7，inline兼容IE7 */
        }
    </style>
</head>
<body>

<div class="content">
    <h2 class="h2">
        测试面包屑
    </h2>
</div>
</body>
</html>
```
