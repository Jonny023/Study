# 通过tree将目录结构写入指定文件

> windows版

```bash
tree /f d:\ > d:\tree.md
```

> mac版
tree -d 只显示文件夹；
tree -L n 显示项目的层级。n表示层级数。比如想要显示项目三层结构，可以用tree -l 3；
tree -I pattern 用于过滤不想要显示的文件或者文件夹。比如你想要过滤项目中的node_modules文件夹，可以使用tree -I "node_modules"；
tree > tree.md 将项目结构输出到tree.md这个文件。

```bash
tree -L 3 -I "node_modules" > tree.md
```
