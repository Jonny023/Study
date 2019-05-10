# Redis存取值

> 清空

```java
while (redisTemplate.opsForList().size("userList") > 0){
    redisTemplate.opsForList().leftPop("userList");
}
```

> 存集合

```java
@Autowired
private RedisTemplate redisTemplate;
redisTemplate.opsForList().rightPushAll("userList", list);
```

> 取集合

```java
@Autowired
private RedisTemplate redisTemplate;
List<Person> oowwoo = redisTemplate.opsForList().range("userList", 0, -1);
```

