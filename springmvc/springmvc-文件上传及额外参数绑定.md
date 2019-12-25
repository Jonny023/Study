### 文件上传额外参数绑定

[参考地址](https://segmentfault.com/q/1010000014944845)

* 控制器方法

```java
package com.jonny.socket.controller;

import com.jonny.socket.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class PostController {

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("/index");
    }

    @RequestMapping("/post/upload")
    @ResponseBody
    public String upload(User user) {
//    public String upload(@RequestParam Map<String, Object> map) {
//        System.out.println(map);
        System.out.println(user);
        return "{\"code\":\"success\", \"msg\": \"操作成功\"}";
    }

    /**
     *  跳转式提交
     *  文件对象和用户对象一并提交
     * @param user
     * @return
     */
    @RequestMapping("/post/normal")
    @ResponseBody
    public String normal(User user, @RequestParam("file") MultipartFile file) {
        System.out.println(user);
        System.out.println(file.isEmpty());
        return "{\"code\":\"success\", \"msg\": \"操作成功\"}";
    }
}
```

* 页面

```html
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>保存</title>
    <style>
        a {
            display: block;
            width: 120px;
            heigt: 45px;
            background: #6b9aff;
            border-radius: 8px;
            text-align: center;
            color: #ffffff;
            text-decoration: none;
            padding: 8px 0px;
        }
    </style>
</head>
<body>

<%--ajax提交--%>
<form>
    username: <input type="text" name="username" id="username"/>
    <br/>
    password: <input type="password" name="password" id="password"/>

    <a href="javascript:void(0)" id="submit">提交</a>
</form>

<%--跳转式页面提交--%>
<form action="${pageContext.request.contextPath}/post/normal" method="post" enctype="multipart/form-data">
    username: <input type="text" name="username" id="username1"/>
    <br/>
    password: <input type="password" name="password" id="password1"/>
    file: <input type="file" name="file"/>

    <input type="submit" value="提交"/>
</form>
<script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>

<script>
    $(function () {
        $("#submit").on("click", function () {
            var username = $("#username").val();
            var password = $("#password").val();
            var formData = new FormData();
            formData.append("username", username);
            formData.append("password", password);
            console.log(username);
            console.log(password);
            // var user = {username: username, password: password};
            // formData.append("user", user);
            $.ajax({
                url: "${pageContext.request.contextPath}/post/upload",
                type: "POST",
                contentType: false,
                // contentType: "application/x-www-form-urlencoded",
                processData: false,
                data: formData,
                dataType: "json",
                success: function (res) {
                    console.log(res);
                }
            });
        });
    });
</script>
</body>
</html>
```
