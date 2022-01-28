### 异步上传文件时获取上传进度信息
> `XMLHttpRequest`实例的`upload`属性可以添加一个事件`progress`,通过该事件回调可以获取到上传进度信息

#### 原生上传

```javascript
//获取文件筐的文件集合
let files = document.getElementById("file").files;
//创建FormData对象
let formData = new FormData();
//添加第一个文件到FormData
formData.append("file",files[0]);
//添加普通数据
formData.append("name","KevinBlandy");
//创建异步对象
let xhr = new XMLHttpRequest();
//打开连接
xhr.open('POST','/test/upload',true);
//监听上传事件
if(xhr.upload){
    //监听上传属性的上传事件,每次上传事件都会执行 progressHandlingFunction
    xhr.upload.addEventListener('progress',progressHandlingFunction, false);
    //xhr.upload.progress = function(){}            也可以
}
//执行上传
xhr.send(formData);
//上传监听
function progressHandlingFunction(event) {
    event.total;        //获取上传文件的总大小
    event.loaded;        //获取已经上传的文件大小
    //获取进度的百分比值
    let percent  = (event.loaded / event.total) * 100;
    //四舍五入保留两位小数
    percent = percent.toFixed(2);
}

```

#### Jquery

```javascript
//获取文件框的文件集合
let files = $('#file')[0].files;
//创建FormData对象
let formData = new FormData();
//添加第一个文件到FormData
formData.append("file",files[0]);
//添加普通属性
formData.append("name","KevinBlandy");
$.ajax({
    url : '${ctxPath}/test/upload',
    type : 'POST',
    data : formData,
    xhr: function(){
        //获取到原生的 XMLHttpRequest 对象
        let myXhr = $.ajaxSettings.xhr();
        //异步上传对象带上传属性
        if(myXhr.upload){            
            //监听上传属性的上传事件,每次上传事件都会执行 progressHandlingFunction
            myXhr.upload.addEventListener('progress',progressHandlingFunction, false);
            //myXhr.upload.progress = function(){}            也可以
        }
        return myXhr;
    },
    processData : false,
    contentType : false,
    success : function(response) {
    },
    error : function(response) {
    },
});
function progressHandlingFunction(event) {
    event.total;        //获取上传文件的总大小
    event.loaded;        //获取已经上传的文件大小
    //获取进度的百分比值
    let percent  = (event.loaded / event.total) * 100;
    //四舍五入保留两位小数
    percent = percent.toFixed(2);
}
```
