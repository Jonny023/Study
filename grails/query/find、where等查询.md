## find查询

```groovy
Person.find {
    department == department && dateCreated >= LocalDateTime.now().minusMonth(1)
}
```

## where查询

```groovy
Person.where {
    department == department && dateCreated >= LocalDateTime.now().minusMonth(1)
}

User.where{
   (username == u.username && nickname == u.nickname) || (addr == u.addr)
}
```
