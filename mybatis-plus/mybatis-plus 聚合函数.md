# Mybatis-plus 聚合函数

## 1.max()获取最大排序号

```java
private Integer getSortNum(MenuSaveOrUpdateVO param) {
	QueryWrapper<UmMenu> wrapper = Wrappers.<UmMenu>query();
    wrapper.eq("app_id", param.getAppId());
	wrapper.eq("type", param.getType());
    wrapper.eq("parent_id", param.getParentId());
    wrapper.select("COALESCE(max(sort_num), 0) as maxSort");
    Map<String, Object> map = super.getMap(wrapper);
    return ((Long) map.get("maxSort")).intValue() + 1;
}
```

