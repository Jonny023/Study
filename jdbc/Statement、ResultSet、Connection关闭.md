[参考](https://blog.csdn.net/weixin_41485724/article/details/115490646)

* 关闭一个statement对象同时也会使得该对象创建的所有resultSet对象被关闭。resultSet所持有的资源不会立刻被释放，直到GC执行， 因此当resultSet对象不再被需要时明确地关闭是一个很好的做法。

> 关闭连接时最好按照resultSet先关，statement接着，connection最后的顺序。
