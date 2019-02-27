> gradle引入本地jar的两种方式

（1）引入所有：
```
compile fileTree(dir: 'lib', includes: ['*.jar']) 
```
（2）单个引入：
```
compile files("lib/xxxx.jar")
```
