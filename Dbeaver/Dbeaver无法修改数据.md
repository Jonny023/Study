### Dbeaver无法修改数据

> 修改表结构或者表数据保存后，刷新看到是改变之后的数据，断开重连后又变回之前的样子

* 原因：工具里面事务提交模式设置为：`手动提交`或`智能提交`模式了，需要手动提交事务

![img](https://github.com/Jonny023/Study/blob/master/Dbeaver/autocommit.png)
