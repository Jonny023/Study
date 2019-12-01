# 如何获取`webapp`路径

* 通过`Request`对象获取

```groovy
request.servletContext.getRealPath("/upload")
request.getSession().getServletContext().getRealPath("/upload")
```

* 通过`GrailsApplication`对象获取

```groovy
File file = grailsApplication.mainContext.getResource("/excelTemp").getFile()
```
