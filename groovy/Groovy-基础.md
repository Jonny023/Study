# Groovy基础

* 分支

```groovy
def person = Expando()
person.name = "Fred"
person.age = 45
def child = 0..16 // inclusive range
def adult = 17.<66 // exclusive range
def senior = 66..120 //
switch(fred.age) {
 case child:
  println "You're too young ${fred.name}!"
  break
 case adult:
  println "Welcome ${fred.name}!"
  break
 case senior:
  println "Welcome ${fred.name}! Take a look at our senior citizen rates!"
  break
}


switch (x) {
 case 'Graeme':
   println "yes it's me"
   break
 case 18..65:
   println "ok you're old enough"
   break
 case ~/Gw?+e/:
   println 'your name starts with G and ends in e!'
   break
 case Date:
   println 'got a Date instance'
   break
 case ['John', 'Ringo', 'Paul', 'George']:
   println "It's one of the beatles! "
   break
 default:
   println "That is something entirely different"
}
```

* 正则

```groovy
mport java.util.regex.*

// 匹配字符以ab开头
assert 'abababab' ==~ /(ab)+/


// Here the pattern operator is used
// to create a java.util.regex.Pattern instances
def pattern = ~/foo/
 assert pattern instanceof Pattern

 // The matcher operator allows you to create a
 // java.util.regex.Matcher instance
 def matcher = "cheesecheese" =~ /cheese/
 assert matcher instanceof
```

* 集合赋值

```groovy
list << 'Chinese' << 'English' << 'Math'
```

* 用指定字符分隔

```groovy
def list = [1,22,10]
list.join(",")
// 1,22,10
```
