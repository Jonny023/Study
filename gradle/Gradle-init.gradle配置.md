 > 在gradle主目录下的`init.d`目录下新建一个`init.gradle`文件，写入内容

```bash
allprojects {
    repositories {
        mavenLocal()
        maven { url "https://repo.huaweicloud.com/repository/maven/" }
        maven { url "http://maven.aliyun.com/nexus/content/groups/public/" }
    }
}

```
   
