# 去重

* 简单集合
```groovy
// 方式一
def list = [1, 2, 3, 2, 4, 1, 5]
println list.unique()  // [1, 2, 3, 4, 5]

// 方式二
def arr = [1,2,1,3,4,4,9]
println arr.unique {a,b -> a <=> b}
println arr as Set
```

* 对象集合

```groovy
def list = [[id: 1,name: '张三'],[id:2,name: '张三'],[id:3, name: '李四']]
println list.unique {it.name}

// 输出[[id:1, name:张三], [id:3, name:李四]]
```
