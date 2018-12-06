在build.gradle中加入excel的依赖

```
compile 'org.grails.plugins:excel-import:3.0.0.RC4'
```
新建一个域类User
```groovy
package com.lee

class User {

    String name  //姓名
    Integer age  //年龄
    String sex   //性别
    String addr  //籍贯

    static constraints = {

        name nullable: false
        age nullable: false,range: 1..100
        sex nullable: false
        addr nullable: false

    }
}
```
创建控制器UserController

注入excelImportService
```groovy
def excelImportService
```

创建文件导入方法
```groovy
def excelImport = {
        InputStream inputStream = null
        try {
            //获取文件，此处可通过上传获取文件
            File file = new File("d:\\用户信息.xlsx")
            //定义表头
            Map CONFIG_BOOK_COLUMN_MAP = [
                    sheet:'Sheet1',
                    startRow: 1,
                    columnMap:  [
                            //Col, Map-Key
                            'A':'name',
                            'B':'age',
                            'C':'sex',
                            'D':'addr'
                    ]
            ]
            //将文件放到输入流中
            inputStream = new FileInputStream(file)
            Workbook workbook = WorkbookFactory.create(inputStream)

            //解析Excel的行存入list
            List list = excelImportService.columns(workbook,CONFIG_BOOK_COLUMN_MAP)
            list.each { v->
                User user = new User()
                user.name = v.name
                user.age = v.age as Integer
                user.sex = v.sex
                user.addr = v.addr
                println "用户信息："+user
                user.save(flush:true)
            }
            render ([msg:"Excel导入成功"]) as JSON
        } catch (Exception e) {
            e.printStackTrace()
            render ([msg:"Excel导入失败"]) as JSON
        }finally{
            //关闭输入流
            if(!inputStream){
                inputStream.close()
            }
        }
    }
```
