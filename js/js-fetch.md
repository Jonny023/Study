# fetch请求

```javascript
fetch(`http://127.0.0.1:8888/actuator/arthas`, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        "Cookie": "_pk_id.1.dda2=5b7af0c5ca863ddc.1650510862.; JSESSIONID=D3C7BAC53424CF4273E79972749EF752"
    },
    mode: "cors"
    // body: `user=user&pass=123`
}).then(response => {
    console.log('响应', response)
    response.json().then(function (data) {
        console.log(data)
    });
}).catch(function (e) {
    console.log(e)
})
```
