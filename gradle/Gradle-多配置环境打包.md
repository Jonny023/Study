## gradle多配置环境打包

> 场景：项目配置以及静态资源分为：dev、uat、prod多个配置文件，打包的时候想要激活指定环境的配置，且打包时只打包对应环境的配置文件和静态资源文件

### 1.项目结构

```sh
src
 ├─main
 │  ├─java
 │  │  └─com
 │  │      └─example
 │  │          └─demo
 │  │              │  Demo1Application.java
 │  │              │
 │  │              └─controller
 │  │                      AppController.java
 │  │
 │  └─resources
 │      │  application-dev.yml
 │      │  application-prod.yml
 │      │  application-uat.yml
 │      │  application.yml
 │      │
 │      ├─env
 │      │  ├─dev
 │      │  │      index.html
 │      │  │
 │      │  ├─prod
 │      │  │      index.html
 │      │  │
 │      │  └─uat
 │      │          index.html
 │      │
 │      └─static
 │              index.html
```

### 2.多环境配置文件

```yaml
# application.yml
spring:
  profiles:
    active: @activeProfile@

# application-dev.yml
app:
  name: dev app

# application-uat.yml
app:
  name: uat app

# application-prod.yml
app:
  name: prod app
```

### 3.静态资源

```sh
src/main/resources/env/dev/index.html
src/main/resources/env/uat/index.html
src/main/resources/env/prod/index.html
```

### 4.build.gradle配置

* task
  * group为分组，配置后会在idea右侧的Gradle中现实对应分组
  * description为任务描述，定义task时需要声明它，不然工具栏看不到
  * dependsOn为执行时需要依赖的task任务
  * finalizedBy为最后执行，相当于是前面执行完成之后再执行后续操作
  * copyFile用来复制对应环境的文件到打包编译目录下
  * 打包jar执行工具栏中对应环境的《打包》命令即可
  * 运行项目直接用工具栏中的《运行》命令

```groovy
plugins {
    id 'org.springframework.boot' version '2.7.2'
    id 'io.spring.dependency-management' version '1.0.12.RELEASE'
    id 'java'
}

group = 'com.example'
version = '0.0.1'
sourceCompatibility = '1.8'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}

//打包源码
task sourceJar(type: Jar) {
    from sourceSets.main.allJava
}

//https://blog.csdn.net/t240034137/article/details/110950761
//https://docs.gradle.org/7.5.1/dsl/org.gradle.api.tasks.Copy.html#org.gradle.api.tasks.Copy:duplicatesStrategy
//https://www.freesion.com/article/7393491721/
def sourceFilter = { env ->
    sourceSets {
        main {
            java {
                srcDir "src/main/java"
            }
            resources {
                //srcDirs = ["src/main/resources/filters/$env"]
                //include("application.yml")
                //include("application-${env}.yml")
                // html文件拷贝到static目录下，必须加斜杠：static/，否则文件夹为空
                includes = ["application.yml", "application-${env}.yml", "static/"]
            }
        }
    }
}

//打包对应环境的资源
def copyFile = { env ->
    copy {
        from this.rootProject.rootDir.path + "/src/main/resources/env/$env/index.html"
        into this.rootProject.rootDir.path + "/src/main/resources/static"
        //include("*.html")
    }
}

tasks.register("dev") {
    group("打包")
    description "dev env"
    dependsOn 'clean'
    doFirst {
        System.setProperty("profile", "dev")
    }
    copyFile("dev")
    sourceFilter("dev")
    finalizedBy("bootJar")

    //根据对应环境编译后，若执行bootRun会删除打包的jar
    //dependsOn 'bootRun'
    //dev.finalizedBy 'bootRun'
}

tasks.register("uat") {
    group("打包")
    description "uat env"
    dependsOn 'clean'
    doFirst {
        System.setProperty("profile", "uat")
    }
    //拷贝对应环境的配置
    copyFile("uat")
    sourceFilter("uat")
    finalizedBy("bootJar")
    //finalizedBy 'bootRun'
}

tasks.register("prod") {
    group("打包")
    description "prod env"
    dependsOn 'clean'
    doFirst {
        System.setProperty("profile", "prod")
    }
    copyFile("prod")
    sourceFilter("prod")
    finalizedBy("bootJar")
    //finalizedBy 'bootRun'
}

processResources {
    doFirst {
        filter org.apache.tools.ant.filters.ReplaceTokens, tokens: [activeProfile: System.getProperty("profile", "dev")]
    }
}

//运行命令，依赖对应的环境
//原本想直接在对应的环境里面调用finalizedBy "bootRun"或dependsOn "bootRun"运行，但经过测试他会把build/libs目录下打包的jar删除掉
task runDev {
    group("运行")
    description "run dev."
    dependsOn "dev"
    finalizedBy "bootRun"
}
task runUat {
    group("运行")
    description "run uat."
    dependsOn "uat"
    finalizedBy "bootRun"
}
task runProd {
    group("运行")
    description "run prod."
    dependsOn "prod"
    finalizedBy "bootRun"
}
```
