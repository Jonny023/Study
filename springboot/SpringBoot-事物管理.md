# Spring事物管理

* 强制回滚

```java
package com.atgenee.base;

import com.atgenee.base.entity.SysPermission;
import com.atgenee.base.entity.SysRole;
import com.atgenee.base.entity.SysUser;
import com.atgenee.base.service.impl.PermissionService;
import com.atgenee.base.service.impl.RoleService;
import com.atgenee.base.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseApplication.class)
//@Transactional(readOnly = false)
@EnableTransactionManagement
public class BaseApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     *  初始化用户信息
     */
    @Test
    @Transactional
    @Rollback(true)
    public void contextLoads() {

        SysRole role = new SysRole();
        role.setRole("ADMIN");
        role.setDescription("超级管理员");
        role.setAvailable(true);
        roleService.addRole(role);

        // 用户
        SysUser user = new SysUser();
        user.setName("Jonny");
        user.setUsername("admin");
        user.setPassword("admin");
        user.setCreateTime(LocalDateTime.now());
        user.setEmail("342418262@qq.com");
        user.setSalt("admin");
        user.setExpiredDate(LocalDateTime.of(2099, 01, 10, 00, 00, 00).toLocalDate());
        user.setRoleList(Arrays.asList(role));
        userService.addUser(user);

        // 权限
        SysPermission permission = new SysPermission();
        permission.setAvailable(true);
        permission.setPermission("*:*");
        permission.setResourceType("menu");
        permission.setAvailable(true);
        permission.setPermissionName("超级管理员所有权限");
        permission.setRoles(Arrays.asList(role));
        permissionService.addUserRole(permission);
    }

}

```
