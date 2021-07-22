# JPA使用笔记

## 1.枚举类应用

> 默认为`@Enumerated(EnumType.ORDINAL)`,`ORDINAL`值默认为`0,1,2,3,4,...,n`

```java
//性别枚举类
public enum SexEnum {

    MAN("男性"), WOMAN("女性");

    private String value;

    private SexEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

//用户
public class User {
    
    @Enumerated(value = EnumType.STRING)
    private SexEnum sex;
}
```

## 2.主键自增

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
```

## 3.日期

```java
@Temporal(TemporalType.TIMESTAMP)
@Column(name = "create_date")
private Date createDate = new Date();
```

## 4.二进制

> 用来保存图片、符文本等较大的数据

```java
@Lob
@Column(name = "content")
private byte[] content;

@Column(name = "description", columnDefinition = "longblob")
private byte[] description;
```

## 5.复杂查询

### 5.1 Specification复杂查询

> `repository`需继承`JpaSpecificationExecutor`

```java
Specification<Student> specification = new Specification<Student>() {
    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        Predicate c1 = criteriaBuilder.equal(root.get("sex"), '男');
        Predicate c2 = criteriaBuilder.equal(root.get("address"), "北京");
        return criteriaBuilder.and(c1, c2);
    }
};
List<Student> list = studentRepository.findAll(specification);
System.out.println(JSON.toJSON(list));

// 写法二
Specification<Student> specification = (Specification<Student>) (root, query, criteriaBuilder) -> {
    Predicate c1 = criteriaBuilder.equal(root.get("sex"), '男');
    Predicate c2 = criteriaBuilder.equal(root.get("address"), "北京");
    return criteriaBuilder.and(c1, c2);
};
List<Student> list = studentRepository.findAll(specification);
System.out.println(JSON.toJSON(list));
```

### 5.2 Criteria+TypedQuery

```java
    @PersistenceContext
    private EntityManager em;

    @Test
    void exe() {
        //查询工厂
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //查询类
        CriteriaQuery<Student> query = cb.createQuery(Student.class);
        Root<Student> stu = query.from(Student.class);
        //查询条件
        List<Predicate> predicates = new LinkedList<>();
        //查询条件设置
        predicates.add(cb.equal(stu.get("sex"), '男'));
        predicates.add(cb.like(stu.get("address"), "北京"));
        //拼接where查询
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        //用JPA 2.0的TypedQuery进行查询
        TypedQuery<Student> typedQuery = em.createQuery(query);
        System.out.println(JSON.toJSONString(typedQuery.getResultList()));
    }

// ----------------------------------------------------------------------------------
// 联表查询
Root<TestContact> testContact = cq.from(TestContact.class);
cq.select(testContact);

Join<TestContact, SystemGroup> groupPath = testContact.join("groups", JoinType.LEFT);
cq.where(cb.or(cb.isEmpty(testContact.get("groups")), groupPath.in(groups)));
```

### 5.3 查询指定列

> [参考地址](https://wiki.eclipse.org/EclipseLink/UserGuide/JPA/Basic_JPA_Development/Querying/Criteria#Constructors)

```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery criteriaQuery = cb.createQuery();
Root root = criteriaQuery.from(Student.class);
criteriaQuery.multiselect(
    root.get("id"),
    root.get("stuName"),
    root.get("address")
);
criteriaQuery.where(cb.equal(root.get("address"), "北京"));
List list = em.createQuery(criteriaQuery).getResultList();
System.out.println(JSON.toJSONString(list));

//select student0_.id as col_0_0_, student0_.stu_name as col_1_0_, student0_.address as col_2_0_ from student student0_ where student0_.address=?
//[[1,"王龙","北京"],[4,"陈汪","北京"]]




//查询指定字段封装到指定类
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery cq = cb.createQuery();
Root e = cq.from(Student.class);
cq.select(cb.construct(StudentVO.class, e.get("id"), e.get("stuName")));
cq.where(cb.equal(e.get("address"), "北京"));
Query query = em.createQuery(cq);
List<StudentVO> result = query.getResultList();
System.out.println(JSON.toJSONString(result));

//select student0_.id as col_0_0_, student0_.stu_name as col_1_0_ from student student0_ where student0_.address=?
//[{"id":1,"stuName":"王龙"},{"id":4,"stuName":"陈汪"}]
```

## 6.事务

```java
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findByLastname(String lastname);

  @Modifying
  @Transactional
  @Query("delete from User u where u.active = false")
  void deleteInactiveUsers();
}
```

## 7.@Lock 锁

```java
interface UserRepository extends Repository<User, Long> {

  // Plain query method
  @Lock(LockModeType.READ)
  List<User> findByLastname(String lastname);
}
```

## 8.建立索引

```java
@Entity
@Table(
    name = "{SPWF_FLOW}",
    indexes = {
        @Index(name = "IDX_TYPE_ZT_XH_ACT", columnList = "{WF_FLOWTYPE_ID},{ZT},{XH},{WF_FLOWVER_ID_ACT}")
    }
)
```

