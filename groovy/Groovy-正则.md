# 正则使用

```groovy
"1234" ==~ /\d+/ // 返回true
"xxdsf" ==~ /\d/ // 返回false


String reg = ~/^\（\d{4}\）\d+$/
def m = ("（2013）001" =~ reg)
def m1 = ("（2013）001a" =~ reg)
println m.find()
println m1.find()
```
