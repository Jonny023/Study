    allprojects {
        repositories {
            mavenLocal()
            maven { url "https://repo.huaweicloud.com/repository/maven/" }
            maven { url "http://maven.aliyun.com/nexus/content/groups/public/" }
        }
    }
