```javascript

document.addEventListener('copy', function (event) {
    var clipboardData = event.clipboardData || window.clipboardData;
    if (!clipboardData) { 
        return; 
    }
    //复制到的文字信息
    var text = window.getSelection().toString();
    if (text) {
        event.preventDefault();
        //修改原来的文字信息，添加版权
        clipboardData.setData('text/plain', text + '\n\njavaweb开发者社区版权所有');
    }
});

javaweb开发者社区版权所有
```
