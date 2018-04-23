[插件官网](http://luis-almeida.github.io/jPages/)

页面引入css和js
```
<link href="/resources/jPages/css/jPages.css" type="text/css" rel="stylesheet" />
<script type="application/javascript" src="/resources/jPages/js/jPages.js"></script>
```
在table中使用jPages分页
```
<table>
    <thead>
       <tr><th></th></tr>
   </thead>
   <tbody id="dataList">
       <tr><td><td></tr>
   </tbody>
</table>
<div class="holder"></div>
```
js部分
```
jQuery('.holder').jPages({
    containerID: 'goods_list',
    first: '首页',//false为不显示
    previous: '上一页',//false为不显示
    next: '下一页',//false为不显示 自定义按钮
    last: '尾页',//false为不显示
    perPage: 10,//每页最多显示10个
    keyBrowse: true,//键盘切换
    animation: 'wobble',
    // scrollBrowse: true,//滚轮切换
    callback: function(pages, items) {
        $(".page-detail").remove();
        $(".holder").append("<span class='page-detail'>共"+pages.count+"页，共"+items.count+"条");
        $(".holder").append();
    },
});
```
