# Intellij IDEA中使用QueryDSL无法找到Q类

> 编译后生成的Q类在输出目录下能找得到，编译不报错，业务类引用Q类报红

##### 解决办法

> `File--Settings(ctrl+alt+s)--Build,Execution,Deployment--Annotation Processors`勾选`Enable annotation processing`

![](https://i.bmp.ovh/imgs/2021/05/76595cee5fb35adf.png)