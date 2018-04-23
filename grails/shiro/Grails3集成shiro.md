集成shiro重点：
```
//build.gradle中新增maven库
maven { url "https://dl.bintray.com/shiranr/plugins" }
maven { url "https://dl.bintray.com/animator013/plugins/" }

//dependencies中新增依赖：
compile 'org.grails.plugins:grails-shiro:3.0.1'
```

通过命令创建域类：
```
shiro-quick-start --prefix=org.example.Shiro.
```

init下面BootStrap.groovy中创建用户
```
import com.system.ShiroPermission
import com.system.ShiroRole
import com.system.ShiroRolePermissionRel
import com.system.ShiroUser
import com.system.ShiroUserRoleRel
import org.apache.shiro.crypto.hash.Sha256Hash

class BootStrap {

    def init = { servletContext ->

        def exists = ShiroUser.findByUsername("admin")

        if(!exists) {

            def user1 = new ShiroUser("admin", new Sha256Hash("admin").toHex()).save(failOnError: true)
            def user2 = new ShiroUser("test", new Sha256Hash("test").toHex()).save(failOnError: true)
            def user3 = new ShiroUser("guest", new Sha256Hash("guest").toHex()).save(failOnError: true)

            def role1 = new ShiroRole("ROLE_ADMIN").save(failOnError: true)
            def role2 = new ShiroRole("ROLE_USER").save(failOnError: true)
            def role3 = new ShiroRole("ROLE_GUEST").save(failOnError: true)

            new ShiroUserRoleRel(user1, role1).save(failOnError: true)
            new ShiroUserRoleRel(user2, role2).save(failOnError: true)
            new ShiroUserRoleRel(user3, role3).save(failOnError: true)

            def p1 = new ShiroPermission("org.apache.shiro.authz.permission.WildcardPermission", "*").save(failOnError: true)

            new ShiroRolePermissionRel(role1, p1, "*:*", "*").save(failOnError: true)
            new ShiroRolePermissionRel(role2, p1, "test:index,create,show,edit,update", "*").save(failOnError: true)
            new ShiroRolePermissionRel(role3, p1, "*:*", "*").save(failOnError: true)

        }
        
    }

    def destroy = {
    }
}
```
### 注意：注释掉auth里面的注销方法里面的：webRequest.getCurrentRequest().session = null这句话，不然会报错
