### JPA自动给创建人、创建时间赋值



* 启动类添加注解`@EnableJpaAuditing`

```java
package cn.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"cn.com.dao.jpa.repository"})
@EnableTransactionManagement
@EnableJpaAuditing
public class TestApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}
}
```

* 添加实现类

```java
package cn.com.jpa.auto;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @description: 自动添加创建人、修改人
 * 配合注解@EnableJpaAuditing、@CreatedBy、@LastModifiedBy、@CreatedDate、@LastModifiedDate
 * @author: Jonny
 * @date: 2021-04-16
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 添加自己的业务
        String username = "admin";
        if (StringUtils.hasText(username)) {
            return Optional.of(username);
        }
        return Optional.empty();
    }
}
```

* 公共基类（父类）

> 创建时间和创建人添加`updatable = false`的原因是，数据修改时不操作这两个属性

```java
package cn.com.jpa.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @description:
 * @author: Jonny
 * @date: 2021-04-16
 */
@Data
@Accessors(chain = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     *  主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     *  业务ID
     */
    @Column(name = "bid")
    private String bid;

    /**
     *  创建时间
     */
    @CreatedDate
    @Column(name = "create_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     *  更新时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     *  创建人
     */
    @CreatedBy
    @Column(name = "create_user", updatable = false)
    private String createUser;

    /**
     *  更新人
     */
    @LastModifiedBy
    @Column(name = "update_user")
    private String updateUser;

}
```

* 实体类继承父类

```java
package cn.com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "test_info")
public class TestEntity extends BaseEntity implements Serializable {

    /**
     *  描述
     */
    @Column(name = "description")
    private String description;

}
```

