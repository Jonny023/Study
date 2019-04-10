# 开发注意事项

* `domain`包名尽量小写
  * 一次包名引发的事件（同事包名用了大小写组合`package`，`idea`中运行正常，打包部署到`tomcat`报错，找不到`domain`类）

* `Domain.withTransaction`和`@Transactional`的区别
  * `withTransaction`将创建会话，`@Transactional`不会

# 打包命令

* 针对不同环境打包
  * `grails war` - 普通打包
  * `grails test war` - 指定测试环境打包
  * `grails -Dgrails.env=staging war` - 指定自定义staging环境打包

# 参数获取

* 普通参数
 * `params.name`
 * `params[name]`
 * `params.get("name")`
 
* 数组/集合
 * `params["ids[]"]`
 * `params.get("ids[]")`
 * 转换为集合/数组
  * `Long[] ids = params["ids[]"].collect {it as Long}`
