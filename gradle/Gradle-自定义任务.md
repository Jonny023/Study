# 文件拷贝（从一个地方拷贝到另一个地方）

```groovy
task copyPDF(type:Copy) {
    dependsOn processResources
    from 'src/main/resources'
    include '*.pdf'
    into 'grails-app/conf/resources'
}
classes {
    dependsOn copyPDF
}
```
