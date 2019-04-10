### 一： 修改表信息

> 1.修改表名 

```mysql
alter table test_a rename to sys_app;
```

> 2.修改表注释   

```mysql
alter table sys_application comment '系统信息表';
```

 

### 二：修改字段信息

> 1.修改字段类型和注释

```mysql
alter table sys_application  modify column app_name varchar(20) COMMENT '应用的名称';
```

> 2.修改字段类型

```mysql
alter table sys_application  modify column app_name text;
```

> 3.单独修改字段注释 

* 目前没发现有单独修改字段注释的命令语句。

> 4.设置字段允许为空

```mysql
alter table sys_application  modify column description varchar(255) null COMMENT '应用描述';
```

> 5.增加一个字段，设好数据类型，且不为空，添加注释

```mysql
alert table sys_application add `url` varchar(255) not null comment '应用访问地址';  
```

> 6.增加主键 

```mysql
alter table t_app add aid int(5) not null ,add primary key (aid);  
```

> 7.增加自增主键

```mysql
alter table t_app add aid int(5) not null auto_increment ,add primary key (aid); 
```

> 8.修改为自增主键

```mysql
alter table t_app  modify column aid int(5) auto_increment ;
```

> 9.修改字段名字(要重新指定该字段的类型)

```mysql
alter table t_app change name app_name varchar(20) not null;
```

> 10.删除字段

```mysql
alter table t_app drop aid; 
```

> 11.在某个字段后增加字段

```mysql
alter table `t_app` add column gateway_id int  not null default 0 AFTER `aid`； #(在哪个字段后面添加)  
```

> 12.调整字段顺序 

```mysql
alter table t_app  change gateway_id gateway_id int not null after aid ; #(注意gateway_id出现了2次)
```
