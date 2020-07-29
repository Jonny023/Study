### #和$区别及用法

* # 预编译（占位符）
* $ 字符拼接

> 代码中必须进行转义

```java
public String findStaffGroups(String staffId) {
    // 查询员工的角色
    String roleStr = staffMapper.getRoleByStaffId(staffId);
    if (org.springframework.util.StringUtils.hasText(roleStr)) {
        String[] roles = roleStr.split(",");
        String groups = Stream.of(roles).collect(Collectors.joining("\",\"", "\"", "\""));
        return groups;
    } else {
        return "\"```\"";
    }
}
```

> Mapper.xml中使用

```xml
select * from table where GROUP_ID_ IN (${GROUPS})
```
