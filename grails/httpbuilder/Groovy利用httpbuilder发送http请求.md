第一种：

Build.gradle
```
compile "io.github.http-builder-ng:http-builder-ng-core:0.17.0"
compile 'junit:junit:4.12'
```
测试方法
```
@org.junit.Test
public void test(){

    def astros = groovyx.net.http.HttpBuilder.configure {
        request.uri = 'http://api.open-notify.org/astros.json'
    }.get()
    println astros

    astros.people.each { p->
        println " - ${p.name} (${p.craft})"
    }
}
```

第二种：

Build.gradle
```
compile group: 'org.codehaus.groovy.modules.http-builder', name: 'http-builder', version: '0.5.0-RC2'
测试方法

@org.junit.Test
void main(){
    def http = new HTTPBuilder('http://api.open-notify.org')
    http.request(Method.GET ,grails.converters.JSON) {req->

        uri.path = "/astros.json"
        headers.'User-Agent' = 'Mozilla/5.0'

        response.success = {resp,json->
            println resp.statusLine  //查看状态
            println resp.statusLine.statusCode == 200 //判断状态码是否为200
            println "Response length: ${resp.headers.'Content-Length'}"  //获取请求头
        }

        //404
        response.'404' = { resp ->
            println 'Not found'
        }

        // 401
        http.handler.'401' = { resp ->
            println "Access denied"
        }

        //其他错误，不实现则采用缺省的：抛出异常。
        http.handler.failure = { resp ->
            println "Unexpected failure: ${resp.statusLine}"
        }
    }}  
```
[参考](https://my.oschina.net/groovyland/blog/3035)
```
package test

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.*
import org.testng.annotations.Test

/**
 * @auther Lee
 * @Date 2017/10/13 12:29
 * return 
 *
 */
class tt {

    def http = new HTTPBuilder("http://v.juhe.cn/weather/index")

    @Test
    void testHttpGet() {
        http.request(Method.GET ,ContentType.JSON) { req ->

            response.success = {resp,json->
                println resp.statusLine  //查看状态
                println resp.statusLine.statusCode == 200 //判断状态码是否为200
                println "Response length: ${resp.headers.'Content-Length'}"  //获取请求头
                println json
            }

            //404
            response.'404' = { resp ->
                println 'Not found'
            }

            // 401
            http.handler.'401' = { resp ->
                println "Access denied"
            }

            //其他错误，不实现则采用缺省的：抛出异常。
            http.handler.failure = { resp ->
                println "Unexpected failure: ${resp.statusLine}"
            }
        }
    }

    @Test
    void testHttpPost() {
        http.request(Method.POST ,ContentType.JSON) { req ->

            response.success = {resp,json->
                println resp.statusLine  //查看状态
                println resp.statusLine.statusCode == 200 //判断状态码是否为200
                println "Response length: ${resp.headers.'Content-Length'}"  //获取请求头
                println json
            }

            //404
            response.'404' = { resp ->
                println 'Not found'
            }

            // 401
            http.handler.'401' = { resp ->
                println "Access denied"
            }

            //其他错误，不实现则采用缺省的：抛出异常。
            http.handler.failure = { resp ->
                println "Unexpected failure: ${resp.statusLine}"
            }
        }
    }
}
```
