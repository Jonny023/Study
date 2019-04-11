# 去重

* 简单集合
```groovy
def list = [1, 2, 3, 2, 4, 1, 5]
println list.unique()  // [1, 2, 3, 4, 5]
```

* 对象集合

```groovy
def list = [[id: 1,name: '张三'],[id:2,name: '张三'],[id:3, name: '李四']]
println list.unique {it.name}

// 输出[[id:1, name:张三], [id:3, name:李四]]
```
