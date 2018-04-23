通过createCriteria可以构建比较复杂的查询条件，包括分页参数的传递、构建left join、right join、inner join等等

新建两个domain类，一个Student和StudentGroup
```
package com.system

class Student {

    String stuName //学生姓名
    String sex     //性别
    Integer age    //年龄
    StudentGroup stuGroup //学生组

    static constraints = {
        stuName nullable: false
        sex nullable: false
        age nullable: false
        stuGroup nullable: false
    }
}
```
```
package com.system

class StudentGroup {

    String groupName //组名
    String groupDesc //组描述

    static constraints = {
        groupName nullable: false,unique: true
        groupDesc nullable: true
    }

    String toString() {
        groupName
    }
}
```
（1）通过学生与学生组进行联表查询，测试left join，此处通过collect闭包对查询结果集进行了封装，在开发过程中可以用它来传递需要的参数，从而优化接口，避免数据量过大。这里只写了left join还有inner join等参数，可自行测试。

注：CriteriaSpecification.LEFT_JOIN已被弃用。可以使用JoinType.LEFT_OUTER_JOIN来获取最新版本。
```
def test() {
    def students = Student.createCriteria().list({
        createAlias("stuGroup","g", CriteriaSpecification.LEFT_JOIN)
        eq("g.groupName",'音乐')
    }).collect{[
            id:it.id,
            stuName: it.stuName,
            sex: it.sex,
            age: it.age,
            group: it.stuGroup
    ]}
    render students as JSON
}
```
打印sql:
```
SELECT
	this_.id AS id1_0_1_,
	this_.version AS version2_0_1_,
	this_.age AS age3_0_1_,
	this_.sex AS sex4_0_1_,
	this_.stu_group_id AS stu_grou5_0_1_,
	this_.stu_name AS stu_name6_0_1_,
	g1_.id AS id1_1_0_,
	g1_.version AS version2_1_0_,
	g1_.group_desc AS group_de3_1_0_,
	g1_.group_name AS group_na4_1_0_
FROM
	student this_
LEFT OUTER JOIN student_group g1_ ON this_.stu_group_id = g1_.id
WHERE
	g1_.group_name =?
```
（2）另一种联表查询，同样可以根据关联表的信息来进行条件过滤，其中stuGroup是Student单向关联的一个字段
```
def find() {
    def students = Student.createCriteria().list({
        stuGroup(CriteriaSpecification.LEFT_JOIN) {
            eq("groupName","音乐")
        }
    }).collect{[
        id:it.id,
        stuName: it.stuName,
        sex: it.sex,
        age: it.age,
        group: it.stuGroup
    ]}
    render students as JSON
}
```
执行sql跟前面一个是一样的
```
SELECT
	this_.id AS id1_0_1_,
	this_.version AS version2_0_1_,
	this_.age AS age3_0_1_,
	this_.sex AS sex4_0_1_,
	this_.stu_group_id AS stu_grou5_0_1_,
	this_.stu_name AS stu_name6_0_1_,
	stugroup_a1_.id AS id1_1_0_,
	stugroup_a1_.version AS version2_1_0_,
	stugroup_a1_.group_desc AS group_de3_1_0_,
	stugroup_a1_.group_name AS group_na4_1_0_
FROM
	student this_
LEFT OUTER JOIN student_group stugroup_a1_ ON this_.stu_group_id = stugroup_a1_.id
WHERE
	(stugroup_a1_.group_name =?)
```
统计和分组
```
def sum() {
    def students = Student.createCriteria().list({
        createAlias("stuGroup","g",CriteriaSpecification.INNER_JOIN)
        projections {
            property("g.groupName") //列出字段，相当于select id,name,...
            sum("id")               //需要求和的字段
            groupProperty("g.groupName") //按stuGroup的groupName分组
        }
    })
    render students as JSON
}
```

sql:
```
SELECT
	g1_.group_name AS y0_,
	sum(this_.id) AS y1_,
	g1_.group_name AS y2_
FROM
	student this_
INNER JOIN student_group g1_ ON this_.stu_group_id = g1_.id
GROUP BY
	g1_.group_name
```
参数列表（一）：

以下参数使用位置：
```
def t() {
    def students = Student.createCriteria().list({
        //此处使用以下参数
    })
}
```
```
between("balance", 500, 1000)                //介于两者之间
eq("branch", "London")                       //等于
eq("branch", "london", [ignoreCase: true])   //等于（忽略大小写）
eqProperty("lastTx", "firstTx")              //两个字段的值相等
gt("balance",1000)                           //大于等于
geProperty("balance", "overdraft")           //前者的值大于等于后者的值
idEq(1)                                      //id等于指定值
ilike("holderFirstName", "Steph%")           //模糊匹配且不区分大小写
'in'("age",[18..65]) or not {'in'("age",[18..65])} //在或不在指定范围（in为保留字段，必须加引号）
isEmpty("transactions")                      //匹配集合为空的
isNotEmpty("transactions")                   //集合属性不为空
isNull("holderGender")                       //匹配为null的字段
isNotNull("holderGender")                    //匹配不为null的字段
lt("balance", 1000)                          //匹配小于特定值
ltProperty("balance", "overdraft")           //一个值小于另一个值
le("balance", 1000)                          //小于等于
leProperty("balance", "overdraft")           //一个值小于等于另一个
like("holderFirstName", "Steph%")            //模糊匹配
ne("branch", "London")                       //不等于指定值
neProperty("lastTx", "firstTx")              //一个属性值不等于另一个属性值
order("holderLastName", "desc")              //按指定属性排序（asc-升序，desc-降序）
rlike("holderFirstName", /Steph.+/)          //匹配正则
sizeEq("transactions", 10)                   //集合大小等于指定值  
sizeGt("transactions", 10)                   //集合大小大于指定值
sizeGe("transactions", 10)                   //集合大小大于等于指定值
sizeLt("transactions", 10)                   //集合大小小于指定值
sizeLe("transactions", 10)                   //集合大小小于等于指定值
sizeNe("transactions", 10)                   //集合大小不等于指定值
sqlRestriction "char_length(first_name) = 4" //使用SQL来修改结果集
firstResult 20                               //起始位置（分页参数）
maxResults 10                                //查询条数（分页参数）
cache true                                   //使用缓存
```
参数列表（二）：

以下参数使用位置：
```
def t() {
    def students = Student.createCriteria().list({
        projections {
            //此处使用以下参数
        }
    })
}
```
下列参数必须在projections里面使用
```
property("firstName")                    //列出查询结果显示字段，相当于select firstName
distinct("fn") or distinct(['fn', 'ln']) //去重条件
avg("age")                               //平均值
count("branch")                          //统计总数
countDistinct("branch")                  //去重统计总数
groupProperty("lastName")                //按指定字段分组
max("age")                               //最大值
min("age")                               //最小值
sum("balance")                           //求和函数
rowCount()                               //总行数
```
