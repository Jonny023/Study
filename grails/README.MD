# 开发注意事项

* `domain`包名尽量小写
  * 一次包名引发的事件（同事包名用了大小写组合`package`，`idea`中运行正常，打包部署到`tomcat`报错，找不到`domain`类）

* `Domain.withTransaction`和`@Transactional`的区别
 * `withTransaction`将创建会话，`@Transactional`不会
