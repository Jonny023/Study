> 交集、并集
```groovy
public static void main(String[] args) {

  def a = [1,2,3,4]
  def b = [2,4]

  //补集,输出[1,3]
  println ((a+b) - a.intersect(b))

  //交集，输出[2,4]
  println a.intersect(b)
}
```

> 分组

```groovy
def lists = [[no: 1, name: '第一'], [no: 1, name: '第一'],[no: 2, name: '第二']]
println "原集合：$lists"

def list = lists.groupBy { it.no }

list.eachWithIndex { l, index ->
    println "分组后：$l.value"
}

// 输出
原集合：[[no:1, name:第一], [no:1, name:第一], [no:2, name:第二]]
分组后：[[no:1, name:第一], [no:1, name:第一]]
分组后：[[no:2, name:第二]]
```


> 通过正则查找满足条件的数据集合
* `(?i)`忽略大小写

```groovy
def list = ["abc","cdf","ccc"]
def lists = list.findAll {c->
    c ==~ /(?i).*c.*/
}
println lists // [abc, cdf, ccc]
```

* `clear` 删除所有元素

```groovy
def list = [1,5,9,2,0,1,5]
list.clear()
assert list == []
```

* `sort` 排序

```groovy
// 升序
list = [1,5,9,2,0,1,5]
list.sort()
assert list == [0,1,1,2,5,5,9]

// 降序
list = [1,5,9,2,0,1,5]
list.sort { it * -1 }
assert list == [9,5,5,2,1,1,0]
```

* `addAll` 添加元素

```groovy
// 添加到末尾
list = [1,5,9,2,0,1,5]
list.addAll([8,6])
assert list == [1,5,9,2,0,1,5,8,6]

// 添加到最前面
list = [1,5,9,2,0,1,5]
list.addAll(0, [8,6])
assert list == [8,6,1,5,9,2,0,1,5]
```

* `unique` 去重

```groovy
list = [1,5,9,2,0,1,5]
list.unique()
assert list == [1,5,9,2,0]


// 根据条件去重，此方法会破坏原集合
list = [1,5,9,2,0,1,5]
list.unique() { it % 3 == 0 }
assert list == [1,9]

// 不破坏原集合数据去重
list = [1,5,9,2,0,1,5]
assert list.unique(false) == [1,5,9,2,0]
assert list == [1,5,9,2,0,1,5]
```

* 初始化

```groovy
list = [1,5,9,2,0,1,5]
```

* `each` 遍历

```groovy
list.each { println it }
```

* `eachWithIndex` 遍历集合（带下标）

```groovy
list.eachWithIndex { val,i -> println "index:${i} value:${val}" }
```

* `first` 获取第一个元素

```groovy
assert list.first() == 1
```

* `max` 最大値

```groovy
assert list.max() == 9
```

* `min` 最小値

```groovy
assert list.min() == 0
```

* `join` 集合中加入分隔符（返回String）

```groovy
assert list.join("*") == "1*5*9*2*0*1*5"
```

* `collect` 对集合进行封装处理

```
assert list.collect { "'${it}'" } == ["'1'", "'5'", "'9'", "'2'", "'0'", "'1'", "'5'"]
```

* `any` 任意满足条件则返回`true`，否则`false`

```groovy
assert list.any { it > 8 }

```

* `every` 所有元素满足条件返回`true`，否则返回`false`

```groovy
assert !list.every { it > 8 }
```

* `find` 返回满足条件的第一个元素

```groovy
assert list.find { it > 4 } == 5
```

* `findAll` 返回满足条件的所有元素集合

```groovy
assert list.findAll { it > 4 } == [5, 9, 5]
```

* `inject` 通过闭包递归处理

```groovy
// inject中的参数在前面，也就是total就是0，递归求和
assert list.inject(0) { total, val -> total + val } == 23

assert list.inject("hello") { str, val -> str + val } == "hello23"

assert list.inject { total, val -> total + val } == 23
```

* `drop` 从指定位置开始到集合最后一个元素位置返回新的集合，不影响原来的数据
```groovy
assert list.drop(2) == [9,2,0,1,5]
```

* `split` 满足条件和不满足条件的分别组成一个集合
```groovy
assert list.split { it % 2 == 0 } == [[2, 0], [1, 5, 9, 1, 5]]
```

* 合并集合
```groovy
assert [1,5,9,2] + [0,1,5] == [1,5,9,2,0,1,5]
```

* 并集（不破坏原结构）
```groovy
def list = [1,5,9,2]
assert list + [0,1,5] == [1,5,9,2,0,1,5]
assert list == [1,5,9,2]
```

* 交集
```groovy
assert [1,5,9,2].plus([0,1,5]) == [1,5,9,2,0,1,5]
```

* 移除集合元素

```groovy
assert [1,5,9,2,0,1,5] - [0,1,5] == [9,2]

def list = [1,5,9,2,0,1,5]
assert list - [0,1,5] == [9,2]
assert list == [1,5,9,2,0,1,5]


assert [1,5,9,2,0,1,5].minus([0,1,5]) == [9,2]
```

* 集合元素在新的集合中出现n次（跟倍数一样）
```groovy
assert [1,2,3] * 3 == [1,2,3,1,2,3,1,2,3]


// 不破坏原数据
def list = [1,2,3]
assert list * 3 == [1,2,3,1,2,3,1,2,3]
assert list == [1,2,3]



// 重复n次集合元素组成新的集合，不破坏原结构
assert [1,2,3].multiply(3) == [1,2,3,1,2,3,1,2,3]
// << 追加元素到集合末尾，破坏原结构
assert [1,2,3] << 5 == [1,2,3,5]
assert [1,2,3] << [5,6] == [1,2,3,[5,6]]


def list = [1,2,3]
assert list << 5 == [1,2,3,5]
assert list == [1,2,3,5]
```

* 基本操作

```groovy

def list2 = list1 * 2    // [1,2,3,1,2,3]
list2.unique()           // [1,2,3]
list1.size()             // 3
list1.reverse()          // [3,2,1]
[1,2,3,4] + [5]          // [1,2,3,4,5]
[1,2,3,4] + 5            // [1,2,3,4,5]
[1,2,3,4] << [5]         // [1,2,3,4,5]
[1,2,3,4,1] - [1]        // [2,3,4]
[1,2,3,4,1] - 1          // [2,3,4]
[1,2,3,[4,5]].flatten()  // [1,2,3,4,5]
[1,2,3,4].max()          // 4
[1,2,3,4].min()          // 1
[1,2,3,4,1].count(1)     // 2
[1,2,3,4].sum()          // 10
[1,3,2,4].sort()         // [1,2,3,4]
def list2 = [[1,1],[2,2],[3,3]]
list2*.unique()          // [[1],[2],[3]]
// *运算符，含义是依次对List每个元素调用*后面的方法
```

* map变量

```groovy
def key = 'key123'
def map1 = [(key): "MyBook"]
// 需要用括号将key的变量括起来
```

* 区间

```groovy
def range1 = 1..5    // range1 长度是5，包含数字5
def range2 = 1..<5   // range2 长度是4，不包含数字5
```

* 分组合并

```groovy
def cartoons = [    
                    'Regular Show',    
                    'The Amazing World of Gumball',    
                    'Adventure Time',    
                    'Uncle Grandpa',    
                    'Batman'    
                ]    
 
//两两合并
def cartoonsSplitListWithTwoCartoonEach = cartoons.collate(2)    
println cartoonsSplitListWithTwoCartoonEach // [[Regular Show, The Amazing World of Gumball], [Adventure Time, Uncle Grandpa], [Batman]]    
 
//三个一组合并
def cartoonsSplitListWithThreeCartoonEach = cartoons.collate(3)    
println cartoonsSplitListWithThreeCartoonEach // [[Regular Show, The Amazing World of Gumball, Adventure Time], [Uncle Grandpa, Batman]]
 
//两个一组合并，舍弃孤立不成组的元素
def cartoonsSplitListWithoutRemainder = cartoons.collate(2, false)    
println cartoonsSplitListWithoutRemainder // [[Regular Show, The Amazing World of Gumball], [Adventure Time, Uncle Grandpa]]
```
