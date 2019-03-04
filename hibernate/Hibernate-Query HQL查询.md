# HQL查询用法

> 数组参数

```java
@Override
public int audit(Integer[] ids, Byte status) {
  String hql = "update Content bean set bean.status=:status where bean.id in (:ids)";
  Query query = getSession().createQuery(hql);
  query.setParameter("status",status); // 单个参数
  query.setParameterList("ids",ids); // 数组
  return query.executeUpdate();
}
```
