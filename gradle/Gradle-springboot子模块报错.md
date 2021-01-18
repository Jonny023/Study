```groovy

plugins {
    id 'org.springframework.boot'
    id 'io.spring.dependency-management'
}

[compileJava,compileTestJava,javadoc]*.options*.encoding = 'UTF-8'
dependencies {
    compile project(':bma-service')
    implementation 'org.springframework.boot:spring-boot-starter'
    compile group: 'org.springframework.boot', name: 'spring-boot-starter-web', version: '2.4.0'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    implementation 'com.baomidou:mybatis-plus-boot-starter:3.0.5'
    implementation 'org.freemarker:freemarker:2.3.30'
}

```
