## Redis中文乱码

> Windows客户端中文乱码

* # 通过命令后面跟上`--raw`显示

```bash
redis-cli --raw 
```

> 代码乱码

```java
public void set(String key, String value, long expiredTime) throws Exception {
    this.set(key.getBytes("utf-8"), value.getBytes("utf-8"), expiredTime);
}
```
