* pom.xml

```xml
		<!--QueryDSL支持-->
		<dependency>
			<groupId>com.querydsl</groupId>
			<artifactId>querydsl-apt</artifactId>
			<scope>provided</scope>
			<exclusions>
				<exclusion>
					<artifactId>javassist</artifactId>
					<groupId>org.javassist</groupId>
				</exclusion>
			</exclusions>
		</dependency>
		<!--QueryDSL支持-->
		
		<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>

			<plugin>
				<groupId>com.mysema.maven</groupId>
				<artifactId>apt-maven-plugin</artifactId>
				<version>1.1.3</version>
				<executions>
					<execution>
						<goals>
							<goal>process</goal>
						</goals>
						<configuration>
							<outputDirectory>target/generated-sources/java</outputDirectory>
							<processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
```



* 配置类

```java
package cn.com.config;

package com.example.springbootjpadruid.config;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;

@Configuration
@EnableJpaRepositories(
//        basePackages = "${spring.jpa.repository}",
        entityManagerFactoryRef = "ckEntityManagerFactoryBean",
        transactionManagerRef = "ckTransactionManager"
)
public class JpaClickHouseConfig {

    /**
     *  数据源名称为：
     *
     * @return
     */
    @Bean(name = "ckDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ck")
    public DataSource ckDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Resource(name = "ckDataSource")
    private DataSource dataSource;

    @Value("${spring.jpa.entity}")
    private String entityBasePackage;

    // JPA扩展配置
    @Resource
    private JpaProperties jpaProperties;

    @Resource
    private HibernateProperties hibernateProperties;

    @Bean(name = "ckJdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(ckDataSource());
    }

    /**
     * 配置第二个实体管理工厂的bean
     *
     * @return
     */
    @Bean(name = "ckEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .properties(getVendorProperties())
                .packages(entityBasePackage)
                .persistenceUnit("ckPersistenceUnit")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        Map<String, String> properties = jpaProperties.getProperties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        return hibernateProperties.determineHibernateProperties(properties, new HibernateSettings());
    }

    /**
     * EntityManager不过解释，用过jpa的应该都了解
     *
     * @return
     */
    @Bean(name = "ckEntityManager")
    public EntityManager entityManager(org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder builder) {
        return SharedEntityManagerCreator.createSharedEntityManager(Objects.requireNonNull(entityManagerFactoryBean(builder).getObject()));
    }

    /**
     * jpa事务管理
     *
     * @return
     */
    @Bean(name = "ckTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryBean(builder).getObject());
    }
}
```



* 工厂配置类

```java
package cn.com.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @description:
 * @author: Jonny
 * @date: 2021-02-25
 */
@Configuration
public class JPAQueryFactoryConfig {

    @Bean
    @Primary
    @Autowired
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    /**
     * ck数据源Factory
     *
     * @param entityManager
     * @return
     */
    @Bean(name = "ckQueryFactory")
    public JPAQueryFactory jpaQueryFactory(@Qualifier("ckEntityManager") EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}

```

