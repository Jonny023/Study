 > 在gradle主目录下的`init.d`目录下新建一个`init.gradle`文件，写入内容

```groovy
allprojects {
    repositories {
		maven { url 'file:///H:/gradle_lib' }
        mavenLocal()
        maven { url "https://repo.huaweicloud.com/repository/maven/" }
        maven { url "http://maven.aliyun.com/nexus/content/groups/public/" }
		mavenCentral()
    }
	
	buildscript { 
        repositories { 
            maven { name "Alibaba"; url 'https://maven.aliyun.com/repository/public' }
			maven { name "huawei"; url 'https://repo.huaweicloud.com/repository/maven/' }
            maven { name "Bstek"; url 'http://nexus.bsdn.org/content/groups/public/' }
            maven { name "M2"; url 'https://plugins.gradle.org/m2/' }
        }
    }
}
```

