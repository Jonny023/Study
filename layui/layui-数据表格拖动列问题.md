> 数据表格拖动列出现重复列

* 原代码

```html
<table class="layui-table" lay-data="{height: 'full-200', width: '100%',height:500, url:'${request.contextPath}/archives/loadDatas?type=${type}', page:true, id:'db'}" lay-filter="demo">
    <thead>
    <tr>
        <th lay-data="{type:'checkbox', fixed: 'left'}"></th>
        <th lay-data="{type:'numbers', fixed: 'left'}"></th>
        <th lay-data="{field:'archivistsName', width: 120}">姓名</th>
        <th lay-data="{field:'licenseNumber', width: 180}">身份证号</th>
        <th lay-data="{field:'dateOfBirth',width: 108, sort: true}">出生日期</th>
        <th lay-data="{field:'filingCabinetNum', width: 65, sort: true, fixed: 'right'}">柜号</th>
        <th lay-data="{field:'recordKonbGrid', width: 65, sort: true, fixed: 'right'}">节号</th>
        <th lay-data="{field:'copies',width: 80, sort: true, fixed: 'right'}">档案号</th>
        <th lay-data="{field:'sex',width: 65}">性别</th>
        <th lay-data="{field:'ethnic',width: 120}">民族</th>
        <th lay-data="{field:'education', width: 110}">学历</th>
        <th lay-data="{field:'archivesTransferTime',width: 120, sort: true, fixed: 'right'}">档案转入时间</th>
        <th lay-data="{field:'transferWays', width: 120, sort: true, fixed: 'right'}">档案转入方式</th>
        <th lay-data="{field:'handlePerson', width: 110, sort: true, fixed: 'right'}">经办人</th>
        <th lay-data="{fixed: 'right', align:'right', minWidth: 120, toolbar: '#barDemo'}"></th>
    </tr>
    </thead>
</table>
```

> 解决方法：固定列不要拖动或者只固定其中某一列（如：第一列/最后一列），其他列都不要加`fixed: 'right'`

* 修改后

```html
<table class="layui-table" lay-data="{height: 'full-200', width: '100%',height:500, url:'${request.contextPath}/archives/loadDatas?type=${type}', page:true, id:'db'}" lay-filter="demo">
    <thead>
    <tr>
        <th lay-data="{type:'checkbox', fixed: 'left'}"></th>
        <th lay-data="{type:'numbers', fixed: 'left'}"></th>
        <th lay-data="{field:'archivistsName', width: 120}">姓名</th>
        <th lay-data="{field:'licenseNumber', width: 180}">身份证号</th>
        <th lay-data="{field:'dateOfBirth',width: 108, sort: true}">出生日期</th>
        <th lay-data="{field:'filingCabinetNum', width: 65, sort: true">柜号</th>
        <th lay-data="{field:'recordKonbGrid', width: 65, sort: true}">节号</th>
        <th lay-data="{field:'copies',width: 80, sort: true,}">档案号</th>
        <th lay-data="{field:'sex',width: 65}">性别</th>
        <th lay-data="{field:'ethnic',width: 120}">民族</th>
        <th lay-data="{field:'education', width: 110}">学历</th>
        <th lay-data="{field:'transferWays', width: 120, sort: true}">档案转入方式</th>
        <th lay-data="{field:'handlePerson', width: 110, sort: true}">经办人</th>
        <th lay-data="{fixed: 'right', align:'right', minWidth: 120, toolbar: '#barDemo'}"></th>
    </tr>
    </thead>
</table>
```
