## 异步编程

```javascript
function get(url, data) {
    return new Pormise((resolve, reject) => {
        $.ajax({
            url: url,
            data: data,
            success: function (data) {
                resolve();
            },
            error: function (err) {
                reject(err);
            }
        });
    });
}

get("/user/get")
    .then((data) => {
    return get(`/get/data?uid=${data.id}`);
})
    .then((data) => {

});
```

