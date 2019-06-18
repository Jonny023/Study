# Python基础语法

## 数（Number）的类型
  * 整数型（`int`） `1,-1,0,20`
  * 长整型（`long`） `8888l,5000l`
  * 布尔型（`bool`） `True,False`
  * 浮点型（`float`） `2.22,0.25`
  * 复合型（`complex`） `2+2j,-2+20j,10+5j`

```python
 # 指定编码
# -*- coding: utf-8 -*-
print("hello world")
print(123)
print('nihao')
print('a')
print('中国'.encode())

# 运算比较
print(1 + 2)
print(1 - 2)
print(1 / 2)
print(1 % 2)
print(1 * 2)
print(1 > 2)
print(1 < 2)
print(1 <= 2)
print(1 >= 2)
print(1 == 2)
print(1 << 2)
print(1 >> 2)
print(0 | 2)
print(5 & 2)

# 变量
a = 20
b = 30
print("定义值为：", a, "，类型为：", type(a), "，a * b = ", a * b)

# 格式化输出
c = 20
print("值：%i大小" % c)
print("hello,%r" % c)
d = "张三"
print("hello,%s" % d)
print("测试%s" % 'abc')

# 重复输出n次
print("." * 3)


# 循环
for i in (0,1,2,3):
    print(i)

# 输入
print("请输入您的名字？")
name = input ()
print("您输入了名字：%s"%name)

# 求绝对值
print(abs(-300))
# 四舍五入
print(round(2.57))

```

## 其他类型
* `String`（字符串）
* `Tuple`（元组）
* `List`（列表）
* `Set`（集合）
* `Dictionary`（字典）

```python
# 转义字符原样输出
s = r'\a'
print(s)

# 字符串
str = "ChongQing"
print(str[0])
print(str[0:2])
print(str[:2])
print(str[2:])

# 列表(元素可以修改)
names = ["张三", "李四", "王五"]
print(names[2])
names[2] = "小王"
print(names[2])

# 元组(元素不能修改)
lists = ("生物", "化学", "物理")
print(lists[1])

# 集合(元素唯一,无序)
setas = set("Hello world ddd")
print(setas)
setbs = set("world")
# 交集
print(setas & setbs)
# 并集
print(setas | setbs)
# 差集
print(setas - setbs)
# 去重
unique = set(setas)
print(unique)

# 字典
k = {"姓名": '张三', "性别": "男"}
print(k["姓名"])
k["姓名"] = "李四"
print(k["姓名"])

# 保留关键字：and、elif、global、or、else、pass、break、continue、import、class、return、for、while
```

## 对象序列化

```python
# 对象序列化
import pickle

# dumps(object)将对象序列化
strsa = ["语文", "数学", "英语"]
strsb = pickle.dumps(strsa)
print(strsb)

# loads(string)将对象恢复，对象类型恢复为序列化之前的类型
strsc = pickle.loads(strsb)
print(strsc)

# dump(object, file)将对象序列化后存到指定文件中
arra = ("张三", "李四", "王五", "小丽")
f1 = open("./data.json", "wb")
pickle.dump(arra, f1, True)
f1.close()

# load(object, file)将序列化后的数据从文件中解析出来
f2 = open("./data.json", "rb")
data = pickle.load(f2)
f2.close()
print(data)

```

### 文件权限

> open(filename,mode)

* `filename`:是一个包含了访问的文件名称的路径字符串

* `mode`:决定了打开文件的模式：只读，写入，追加等，默认文件访问模式为只读(r)

* 不同模式打开文件的列表：

|参数|说明|
|--|--|
|r|以只读的方式打开文件，文件的指针将会放在文件的开头，为默认模式|
|rb|以二进制格式打开一个文件用于只读，文件指针会在文件的开头|
|r+|打开一个文件用于读写，文件指针将会在文件的开头|
|rb+|以二进制格式打开一个文件用于读写，文件指针会放在文件的开头|
|w|打开一个文件用于写入，如果该文件已存在则将会覆盖文件，如果不存在则创建新文件|
|wb|以二进制打开一个文件用于写入|
|w+|打开一个文件用于读写|
|wb+|以二进制格式打开一个文件用于读写，如果文件存在则覆盖，如果不存在则创建新文件|
|a|打开一个文件用于追加内容，如果文件已存在，文件指针会放在文件的结尾，如果不存在则创建新文件进行写入|
|ab|以二进制格式打开一个文件用于追加写入|
|a+|打开一个文件用于读写，如果该文件已存在，文件指针会放在结尾，文件打开时会是追加模式，该文件不存在则创建新文件|
|ab+|以二进制格式打开一个文件用于追加。|

### 其他语法

```python
a = 0;
if a < 3:
    print(a)

while a < 5:
    print(a)
    a+=1
```

