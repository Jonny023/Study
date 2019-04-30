# SpringSecurity 常用

* 获取登录用户

```java

UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//details里面可能存放了当前登录用户的详细信息，也可以通过cast后拿到
User userDetails = (User) authenticationToken.getDetails();


Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if (!(authentication instanceof AnonymousAuthenticationToken)) {
    String currentUserName = authentication.getName();
    return currentUserName;
}
```
