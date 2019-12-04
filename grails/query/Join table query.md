# 联表查询

```groovy
List list = AuditIssues.withCriteria {
    def sub = org.hibernate.criterion.DetachedCriteria.forClass(PartAuditInfo.class, 'p').with {
        add(Restrictions.eq('id', 51L))
        setProjection(org.hibernate.criterion.Property.forName('id'))
    }
    add(Subqueries.propertyIn('partId', sub))
}
```
