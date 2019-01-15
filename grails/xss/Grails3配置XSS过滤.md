描述：
项目部署上线后站点安全尤为重要，配置XSS过滤有助于降低XSS注入攻击的风险，本教程不对XSS作过多说明，闲言少叙，上菜。

1、创建项目，并在build.gradle中引入
```
//xss插件
compile 'org.jsoup:jsoup:1.11.2'
//fastjson
compile group: 'com.alibaba', name: 'fastjson', version: '1.2.44'

```
Maven仓库地址添加阿里的仓库地址：
//配置全局仓库

```gradle
allprojects {
    repositories {
        mavenLocal()
        maven { url "http://maven.aliyun.com/nexus/content/groups/public" }
        maven { url "https://repo.grails.org/grails/core" }
    }
}

```
JsoupUtils

```groovy
package com.filter

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

import java.nio.charset.StandardCharsets

/**
 * @auther Lee
 * @Date 2018/3/13 11:33
 * return
 */
public class JsoupUtils {

    //一个白名单,里面配置允许出现的html标签,属性,特殊标签的特殊属性,强制添加的属性等等
    private static final Whitelist XSS_LIST = Whitelist.none()

    //一个白名单,准确的说是一个空的白名单,因为里面不允许出现任何html代码
    private static final Whitelist WHITELIST = Whitelist.none()

    /**
     * 过滤所有的HTML元素,一般用于过滤标题的xss代码,标题中不会允许有html代码的存在
     * @param content
     * @return
     */
    public static String cleanHTML(String content) {
        return content == null ? null : Jsoup.clean(content, WHITELIST)
    }

    /**
     * 过滤XSS字符,
     * @param content
     * @return
     */
    public static String cleanXss(String content) {
        return content == null ? null : Jsoup.clean(content, XSS_LIST)
    }

    static {
        //加载类时候读取配置的JSON文件
        Resource resource = new ClassPathResource("xss-white.json")
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
            StringBuilder stringBuilder = new StringBuilder()
            String line = null
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line.trim())
            }

            JSONObject jsonObject = JSON.parseObject(stringBuilder.toString())

            //允许标签
            JSONArray tags = jsonObject.getJSONArray("allow_tags")
            XSS_LIST.addTags(tags.toArray(new String[tags.size()]))

            //允许属性
            JSONArray properties = jsonObject.getJSONArray("allow_properties")
            XSS_LIST.addAttributes(":all",properties.toArray(new String[properties.size()]))

            //允许特殊属性
            JSONObject specialProperties = jsonObject.getJSONObject("special_properties")
            specialProperties.keySet().each {tag ->
                JSONArray attributes = specialProperties.getJSONArray(tag)
                XSS_LIST.addAttributes(tag,attributes.toArray(new String[attributes.size()]))
            }

            //允许特殊协议
            JSONObject protocols = jsonObject.getJSONObject("protocols")
            protocols.keySet().each{tag ->
                JSONObject protoObject = protocols.getJSONObject(tag)
                protoObject.keySet().each {attr ->
                    JSONArray protocolValues = protoObject.getJSONArray(attr)
                    XSS_LIST.addProtocols(tag,attr,protocolValues.toArray(new String[protocolValues.size()]))
                }
            }

            //固定属性值,非必须的
            JSONObject fixedProperties = jsonObject.getJSONObject("fixed_properties")
            if(fixedProperties != null && !fixedProperties.isEmpty()) {
                fixedProperties.keySet().each{tag ->
                    JSONObject property = fixedProperties.getJSONObject(tag)
                    if(property != null && !property.isEmpty()) {
                        property.keySet().each {attr ->
                            String value = property.getString(attr)
                            XSS_LIST.addEnforcedAttribute(tag, attr, value)
                        }
                    }
                }
            }
        } catch (e) {
            e.printStackTrace()
            throw new RuntimeException("加载XSS过滤白名单异常,请检查文件 xss-white.json")
        }
    }
}

```
2、在grails-app/controllers下面新建拦截器XssInterceptor,在before方法中调用刚才的xss过滤方法

```groovy
package com.interceptor

import com.filter.JsoupUtils

/**
 *  xss拦截器
*/
class XssInterceptor {

  XssInterceptor() {
          matchAll()
  //        match(controller: "*",action: "*").except(action: "login")
  }

  boolean before() {
    params.each {
      if(it.value && it.value instanceof String) {
              it.value = JsoupUtils.cleanXss(it.value)
          }
      }
    true
  }

  boolean after() { true }

  void afterView() {
  // no-op
  }
}

```
3、还需添加一个xss-white.json文件到grails-app/conf目录下

xss-white.json内容:

```json
{
  "allow_tags": [
    "a","abbr","acronym","address","area","article","aside","audio",
    "b","bdi","big","blockquote","br",
    "caption","cite","code","col","colgroup",
    "datalist","dd","del","details","div","dl","dt",
    "em",
    "fieldset","figcaption","figure","footer",
    "h1","h2","h3","h4","h5","h6","hr",
    "i","img","li","ins","font",
    "ol",
    "p","pre",
    "q",
    "u","ul",
    "small","span",
    "table","tr","td","tbody","thead",
    "video","strike"
  ],
  "allow_properties":[
    "style","title","class","id","controls","width","height","face","target"
  ],
  "special_properties": {
    "a":["href"],
    "img":["src"]
  },
  "protocols": {
    "a": {
      "href":["#","http","https","ftp","mailto"]
    },
    "blockquote":{
      "cite":["http","https"]
    },
    "cite":{
      "cite":["http","https"]
    },
    "q":{
      "cite":["http","https"]
    }
  },
  "fixed_properties":{
    "tag":{
      "attr":"value"
    }
  },
  "desc":{
    "allow_tags":"仅仅允许使用的html标签",
    "allow_properties":"所有标签都允许使用的属性",
    "special_properties":"特殊标签允许使用的特殊属性",
    "protocols":"特殊标签,特殊属性,仅仅允许使用指定的协议",
    "fixed_properties":"特殊标签,必须会添加的固定属性与值(会覆盖原来的)"
  }
}

```
注意：使用这个util有个问题，比如：<a href="/xxx/xxx.doc">附件</a>这种/xxx/xxx格式的相对路径，默认都会被过滤，如果不想过滤，需要换一个方法

此处的网址必须添加上，任意http://打头的都可以不管能不能访问，去掉或者换成普通字符就不行，切记！！！

```java
return content == null ? null : Jsoup.clean(content,"http://www.baidu.com",XSS_LIST.preserveRelativeLinks(true));

```


Java通用版

```java
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @auther Lee
 * @Date 2018/3/22 10:35
 * return
 */
public class JsoupUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsoupUtils.class);

    //一个白名单,里面配置允许出现的html标签,属性,特殊标签的特殊属性,强制添加的属性等等
    private static final Whitelist XSS_LIST = Whitelist.none();

    //一个白名单,准确的说是一个空的白名单,因为里面不允许出现任何html代码
    private static final Whitelist WHITELIST = Whitelist.none();

    /**
     * 过滤所有的HTML元素,一般用于过滤标题的xss代码,标题中不会允许有html代码的存在
     * @param content
     * @return
     */
    public static String cleanHTML(String content) {
        return content == null ? null : Jsoup.clean(content, WHITELIST);
    }

    /**
     * 过滤XSS字符,
     * @param content
     * @return
     */
    public static String cleanXss(String content) {
        return content == null ? null : Jsoup.clean(content, "http://base.uri", XSS_LIST.preserveRelativeLinks(true));
    }

    static {
        //加载类时候读取配置的JSON文件
        Resource resource = new ClassPathResource("xss-white.json");
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))){
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line.trim());
            }

            JSONObject jsonObject = JSON.parseObject(stringBuilder.toString());

            //允许标签
            JSONArray tags = jsonObject.getJSONArray("allow_tags");
            XSS_LIST.addTags(tags.toArray(new String[tags.size()]));
            LOGGER.info("允许标签:{}", tags);

            //允许属性
            JSONArray properties = jsonObject.getJSONArray("allow_properties");
            XSS_LIST.addAttributes(":all",properties.toArray(new String[properties.size()]));
            LOGGER.info("允许属性:{}",properties);

            //允许特殊属性
            JSONObject specialProperties = jsonObject.getJSONObject("special_properties");
            for(String tag:specialProperties.keySet()) {
                JSONArray attributes = specialProperties.getJSONArray(tag);
                XSS_LIST.addAttributes(tag,attributes.toArray(new String[attributes.size()]));
                LOGGER.info("允许特殊属性:标签={},属性={}",tag,attributes);
            }

            //允许特殊协议
            JSONObject protocols = jsonObject.getJSONObject("protocols");
            for(String tag:protocols.keySet()) {
                JSONObject protoObject = protocols.getJSONObject(tag);
                for(String attr:protoObject.keySet()) {
                    JSONArray protocolValues = protoObject.getJSONArray(attr);
                    XSS_LIST.addProtocols(tag,attr,protocolValues.toArray(new String[protocolValues.size()]));
                    LOGGER.info("允许特殊协议:标签={},属性={},协议={}",tag,attr,protocolValues);
                }
            }

            //固定属性值,非必须的
            JSONObject fixedProperties = jsonObject.getJSONObject("fixed_properties");
            if(fixedProperties != null && !fixedProperties.isEmpty()) {
                for(String tag:fixedProperties.keySet()) {
                    JSONObject property = fixedProperties.getJSONObject(tag);
                    if(property != null && !property.isEmpty()) {
                        for(String attr:property.keySet()) {
                            String value = property.getString(attr);
                            XSS_LIST.addEnforcedAttribute(tag, attr, value);
                            LOGGER.info("强制属性:标签={},属性={},值={}",tag,attr,value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加载XSS过滤白名单异常,请检查文件 xss-white.json");
        }
    }

    public static void main(String[] args) {
        String content = "<a href='javascript:;' onclick='alert();'>你好</a>";
        String str = "<a href='/index' onclick='alert();'>xxx</a>";
        System.out.println(cleanXss(content));
        System.out.println(cleanXss(str));
    }
}

```
Java版实现过滤所有请求方法：继承HttpServletRequestWrapper类，重写getParameter、getParameterValues、getParameterNames方法

```java
package com.filter;

import com.wemall.core.tools.JsoupUtils;
import org.apache.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * @author admin
 * @Date 2018/3/22 11:52
 * return
 */
public class XssRequest extends HttpServletRequestWrapper {

  private Map<String, String[]> sanitizedQueryString;

  XssRequest(HttpServletRequest request) {
    super(request);
  }

// 重写getParameter()方法
@Override
public String getParameter(String name) {
  // 返回过滤后的参数值
  String parameter = null;
  String[] vals = getParameterMap().get(name);

  if (vals != null && vals.length > 0) {
      parameter = vals[0];
  }
  return parameter;
}

  // 重写getParameterValues()方法
  @Override
  public String[] getParameterValues(String name) {
    return getParameterMap().get(name);
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    if(sanitizedQueryString == null) {
      Map<String, String[]> res = new HashMap<String, String[]>();
      Map<String, String[]> originalQueryString = super.getParameterMap();
      if(originalQueryString!=null) {
        for (String key : (Set<String>) originalQueryString.keySet()) {
            String[] rawVals = originalQueryString.get(key);
            String[] snzVals = new String[rawVals.length];
            for (int i=0; i < rawVals.length; i++) {
              snzVals[i] = JsoupUtils.cleanXss(rawVals[i]);
            }
          res.put(JsoupUtils.cleanXss(key), snzVals);
        }
      }
     sanitizedQueryString = res;
    }
    return sanitizedQueryString;
  }

  @Override
  public Enumeration<String> getParameterNames() {
    return Collections.enumeration(getParameterMap().keySet());
  }
}

```
