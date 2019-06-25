# 获取radio选中值

```javascript

<div class="mui-table-view-radio mui-radio mui-pull-left mui-col-xs-2">
    <input name="id" type="radio" value="${d?.id}" class="mui-radio-sel">
</div>

selected("mui-radio-sel");

function selected(className) {
    var rdsObj = document.getElementsByClassName(className);
    console.log(rdsObj);
    var chackVal = null;
    for(i = 0; i < rdsObj.length; i++) {
        if (rdsObj[i].checked) {
            chackVal = rdsObj[i].value;
        }
    }
    return chackVal;
}
```
