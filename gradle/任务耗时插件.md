> build.gradle配置
```
buildscript {
    repositories {
        jcenter()
        maven(){
            url 'https://dl.bintray.com/codingbingo/maven'
        }
    }
    dependencies {
        classpath 'com.codingbingo.library:gradletaskrecord:1.0.0'
    }
}

apply plugin: 'com.codingbingo.gradletaskrecord'
```
