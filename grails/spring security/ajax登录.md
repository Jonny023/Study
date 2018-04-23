请求地址：http://localhost:8080/mztj/login/authenticate?ajax=true

页面ajax:
```
<script>
  $.post("http://localhost:8080/mztj/login/authenticate?ajax=true",$("form").serialize(),function (data) {
    //处理登录信息
  })
</script>
```
