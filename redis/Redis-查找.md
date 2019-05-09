## Redis查找指定key

> `spring`中`RedisTemplate`使用`scan`代替`keys`
`keys *`这个命令千万别在生产环境乱用。特别是数据庞大的情况下。很多公司的运维都是禁止了这个命令的
当需要扫描`key`，匹配出自己需要的`key`时，可以使用`scan`命令

```java
@Component
public class RedisHelper {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public void scan(String pattern, Consumer<byte[]> consumer) {
        this.stringRedisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }
}
//调用代码
this.scan("*", item -> {
    //符合条件的key
    String key = new String(item,StandardCharsets.UTF_8);
});

```
