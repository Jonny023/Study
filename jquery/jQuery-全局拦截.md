# jQuery全局状态码、权限拦截

```javascript
$(function(){  
    $.ajaxSetup({
        type: "POST",
        error: function(jqXHR, textStatus, errorThrown) {
            switch(jqXHR.status) {
              case(500):
                alert("服务器系统内部错误");
                break;
               case(401):
                alert("未登录");
                break;
               case(403):
                alert("无权限执行此操作");
                break;
               case(408):
                alert("请求超时");
                break;
              default:
                alert("未知错误");
             }
        }
   });
});

// 二

$(document).ajaxComplete(function(ev, xhr, settings) {
    var res = xhr.responseText;
    try {
        if (res == 'Unauthorized') {
           layer.msg("登录超时，请刷新登录");
        }
    } catch (e) {}
});
```

// 三

```js
var token = localStorage.getItem("token");
 
$.ajaxSetup({
    dataType: "json",
    cache: false,
    headers: {
        "token": token
    },
    xhrFields: {
        withCredentials: true
    },
    complete: function(xhr) {
        //token过期，则跳转到登录页面
        if(xhr.responseJSON.code == 401){
            parent.location.href = baseURL + 'login.html';
        }
    }
});
```
