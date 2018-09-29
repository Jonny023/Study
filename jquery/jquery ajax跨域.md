### 造成跨域的原因
  * ajax请求
  * 不同端口、ip、域名等

### 使用例子

```

function test() {
    $.ajax({
        url: DATAACCESS,
        data:{
            datasourceId: "40288fb85a64e90b015a69287dd30004",
            paramMap: "{\"P_YHM\":\"aly004\"}"
        },
        async: true,
        type: 'post',
        dataType: 'json',
        crossDomain: true, //最重要的地方，为true表示强制跨域，或者使用jsonp: "jsoncallback",
        timeout: 10000,
        // jsonp: "jsoncallback",
        success:function(data) {
            //success do something
        },
        error:function() {
            //error
        }
    });
}

//另一种
$.ajax({
  url: 'https://example.com/auth',
  method: 'post',
  xhrFields: { withCredentials: true },
  dataType: 'json',
  data: {
    email: self.get('email'),
    password: self.get('password'),
    authenticity_token: $('meta[name=csrf-token]').attr('content')
  }
}).then(function(response) { ... });

/* 初始化上传信息 */
function initConfig() {
	$.ajax({
		url: prefix + '/init',
		data: '',
		async: false,
		dataType: "jsonp",
		jsonp: "callbackparam", //服务端用于接收callback调用的function名的参数   
		jsonpCallback: "success_jsonpCallback", //callback的function名称,服务端会把名称和data一起传递回来 
		success: function(result) {
			uploaderInit();
		},
		error: function() {
			alert("上传信息初始化失败!");
		},
		timeout: 3000
	});
}

function success_jsonpCallback(obj) {
	if(obj.length > 0 && obj[0].jid) {
		var id = obj[0].jid;
		jid = ';jsessionid=' + id;
	} else {
		alert("上传信息初始化失败!");
	}
}  

//带token认证
$.ajax({
    type: "POST",
    crossDomain: true,
    beforeSend: function(request) {
        request.setRequestHeader('Authorization', localStorage.access_token); 
    },
    url: baseUrl+"/api/endpoint1"
}).done(function(response) {
    // do stuff with response
});          
```
