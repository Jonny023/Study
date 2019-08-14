## 返回json数据排除敏感字段

* `json`注册

```groovy
JSON.registerObjectMarshaller(User) {
    def output = [:]
    output['id'] = it.id
    output['username'] = it.username
    return output;
}
```

* 定制类，`resources.groovy`中

```groovy
userRenderer(JsonRenderer, User) {
    excludes = ['password']
}
```

* 返回时处理

```groovy
def user = User.list().collect {
  [
    id: it?.id,
    username: it?.username
  ]
}
```
