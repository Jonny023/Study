***一、获取数组、集合指定位置元素***
~~~groovy
//获取数组指定位置元素
def arrs = 0..10

//第一个元素
println arrs[0]
//此种方式获取仅限a..b这样定义的数组
println arrs.from

//最后一个元素
println arrs[-1]
//此种方式获取仅限a..b这样定义的数组
println arrs.to

def lists = new ArrayList()
lists.add([id: 1,name: "小明"])
lists.add([id: 2,name: "小张"])
lists.add([id: 3,name: "小飞"])
println lists[-1]
~~~

~~~groovy
//获取指定条件元素
println lists.find {it.id>1}

def arr = [10,8,5,100,20,40]

//通过jdk自带方法获取数组最大值和最小值
println Collections.min(arr)
println Collections.max(arr)

//通过groovy内置方法获取数组最大值和最小值
println arr.min()
println arr.max()

def ars = ["aaa","bbb","cccc"]
//获取数组字符最长的值和字符最短值
println ars.min {it.size()}
println ars.max {it.size()}
//数组排序
println arr.sort()
//将数组顺序倒过来
println ars.reverse()
//移除最后一个元素
println ars - ars[-1]
~~~

***二、动态性***

*groovy每个对象都有一个原类metaClass，可以通过metaClass访问该原类，动态删减方法，如下，通过调用metaClass给String添加了一个名为say的方法，在该方法中对字母进行转换，所有字母转为大写*

~~~groovy
def strs = "hello"
def abc = "abcdef"
println strs.metaClass
String.metaClass.say = {delegate.toUpperCase()}
println strs.say()
println abc.say()
//输出结果
HELLO
ABCDEF
~~~

***三、将对象转为json字符串***

~~~groovy
def jsonString = JsonOutput.toJson([name:"张三",age:20])

//将json字符串转换为json对象
JsonSlurper jsonSlurper = new JsonSlurper()
def jsonObject = jsonSlurper.parseText(jsonString)
println jsonObject.name
~~~
