## 遇到的错误

* 序列化为json字符串时报错
```
cannot deserialize from Object value (no delegate- or property-based Creator)
```

* 解决方法是给实体类添加一个空的构造器
