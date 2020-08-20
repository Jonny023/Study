## 正则替换

```javascript
url = "http://localhost:8086/usercenter/userId?nlzx_t=111&name=zy&nlzx_sign=200"
url.replace(/\?[\x00-\xff]*/g, "")
//output
"http://localhost:8086/usercenter/userId"
```
