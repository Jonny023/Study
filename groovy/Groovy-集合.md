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

> 查找满足条件的数据集合

```groovy
def list = ["abc","cdf","ccc"]
def lists = list.findAll {c->
    c ==~ /(?i).*c.*/
}
println lists // [abc, cdf, ccc]
```
