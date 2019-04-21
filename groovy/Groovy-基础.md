# Groovy基础

* 分支

```groovy
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
