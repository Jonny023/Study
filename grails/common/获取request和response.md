# 如果在服务类中获取上下文

```groovy
def webUtils = WebUtils.retrieveGrailsWebRequest()
HttpServletRequest request = webUtils.getCurrentRequest()
HttpServletResponse response = webUtils.getCurrentResponse()
```
