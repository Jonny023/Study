# `dependencyManagement`的作用

* `parent`：模块间根据这个标签进行继承关系。 
* `dependency`：管理依赖jar，父模块（`parent`）中`dependency`默认，继承到子模块中。 
* `dependencyManagement`：统一管理`dependency`中的`version`，而且默认不继承；
但是管理子模块中相同`dependency`的`version`（即子模块，配置相同的`dependency`不用显示配置`version`）。
  * 如果子模块没有配置`version`，会去`dependencyManagement`找对应模块的`version`，若没有就报错
  * 如果子模块有配置`version`，则以子模块的`version`为准
