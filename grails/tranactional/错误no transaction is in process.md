* `no transaction is in process`错误

```java
UserRole.withTransaction { status ->
    UserRole.withSession {
        it.flush()
        it.clear()
    }
}
```
