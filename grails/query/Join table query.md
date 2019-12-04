# 联表查询

```groovy
List list = User.withCriteria {
    def sub = org.hibernate.criterion.DetachedCriteria.forClass(UserRole.class, 'p').with {
        add(Restrictions.eq('id', 51L))
        setProjection(org.hibernate.criterion.Property.forName('id'))
    }
    add(Subqueries.propertyIn('roleId', sub))
}
```
