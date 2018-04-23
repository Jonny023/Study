创建简单的xml
```
/**
 *  创建简单的xml
 */
def createXml() {
    def sw = new StringWriter()
    MarkupBuilder mk = new MarkupBuilder(sw)
    mk.users {
        user {
            name("张三")
            age(20)
            sex("男")
            addr("山东")
        }
        user {
            name("小丽")
            age(18)
            sex("女")
            addr("北京")
        }
        user {
            name("小王")
            age(22)
            sex("男")
            addr("贵州")
        }
    }
    println sw
    render "简单的xml"

}         
```
效果：
```
<users>
  <user>
    <name>张三</name>
    <age>20</age>
    <sex>男</sex>
    <addr>山东</addr>
  </user>
  <user>
    <name>小丽</name>
    <age>18</age>
    <sex>女</sex>
    <addr>北京</addr>
  </user>
  <user>
    <name>小王</name>
    <age>22</age>
    <sex>男</sex>
    <addr>贵州</addr>
  </user>
</users> 
```
创建复杂的xml
```
/**
 * 创建复杂的xml
 * @return
 */
def complexXml() {
    def builder=new groovy.xml.StreamingMarkupBuilder()
    builder.encoding='utf-8' //设置编码
    def person = {
        mkp.xmlDeclaration() //<?xml version='1.0'?>
        mkp.declareNamespace("info":"userInfo") //添加命名空间
        mkp.declareNamespace("location":"http://www.baidu.com")
        persons {
            comment << "用户信息"  //添加注释
            info.person(id:3){
                name("张三")
                age(22)
                sex("男")
                location.addr('重庆')
            }
            info.person(id:1){
                name("李四")
                age(20)
                sex("男")
                location.addr('北京')
            }
            info.person(id:2){
                name("小丽")
                age(19)
                sex("女")
                location.addr('天津')
            }
        }

    }
    def writer = new StringWriter()
    writer << builder.bind(person)
    def file = new File("d:\\user.xml")
    def pw = new PrintWriter(file)
    pw.print(writer)
    pw.flush()
    pw.close()
    println writer
    render "复杂"
}
```
解析xml
```
def readXml() {
    params.remove("controller")
    params.remove("format")
    params.remove("action")
    def xml = new XmlSlurper()
    def result = xml.parse(new File("d:\\usr.xml"))
    result.user.each {k->
        params.name = k.name.toString()
        params.age  = k.age
        params.sex  = k.sex.toString()
        params.addr = k.addr.toString()
        User user = new User(params)
        user.save(flush:true)
        //打印错误
        //println user.errors
    }
    render "解析成功"
}
```
