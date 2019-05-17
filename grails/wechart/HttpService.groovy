package com.prospect.utils

import com.prospect.dolphin.ConfigUtils
import com.prospect.dolphin.JsonUtils
import grails.converters.JSON
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.config.SocketConfig
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.protocol.HTTP
import org.apache.http.util.EntityUtils

class HttpService {

    static void main(String[] ages){
        String posturl = "https://ocrapi-document.taobao.com/ocrservice/document"
        def header = ["Authorization":"APPCODE 6a6099f729c94b5ba54241ebf1061325","Content-Type":"application/json; charset=UTF-8"]
        for (int i = 9; i <= 20; i++) {
            String count = i  < 10 ? "0" + i : i + ""
            String bodyJson = JsonUtils.objToJsonStr(["url":"http://pmp.ywyh.top/IMG_72" + count + ".JPG", "prob": true])
            new HttpService().postJson(posturl, bodyJson, header).entity.prism_wordsInfo.each{
                println it.word
            }
            println "--------------------------------------------------------------------------------------------------------"
        }
    }


    def getJson(String requestUrl, def param = null, def headers = null){
        def result = get(requestUrl, param, headers)
        result.entity = result.entity ? JSON.parse(result.entity) : null
        return result
    }

    def postJson(String requestUrl, def param = null, def headers = null){
        def result = post(requestUrl, param, headers)
        result.entity = result.entity ? JSON.parse(result.entity) : null
        return result
    }

    def get(String url, def params = null, def headers = null){
        if(params){
            boolean isBegin = true
            for(Map.Entry<String,Object> entry : params.entrySet()){
                if(isBegin){
                    url += "?" + entry.getKey() + "=" + entry.getValue()
                    isBegin = false
                }else{
                    url += "&" + entry.getKey() + "=" + entry.getValue()
                }
            }
        }
        HttpGet get = new HttpGet(url);
        if(headers){
            for(Map.Entry<String,Object> entry : headers.entrySet()){
                if(entry.getValue()!=""){
                    get.setHeader(entry.getKey(), entry.getValue() as String)
                }
            }
        }
        return http(get)
    }

    def post(String url, def params = null, def headers = null){
        HttpPost post = new HttpPost(url)
        if(params && params instanceof Map){
            List <NameValuePair> paramList = new ArrayList<NameValuePair>()
            for(Map.Entry<String,Object> entry : params.entrySet()){
                if(entry.getValue()!=""){
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue() as String))
                }
            }
            post.setEntity(new UrlEncodedFormEntity(paramList,HTTP.UTF_8))
        }else{
            StringEntity entity = new StringEntity(params.toString(),"utf-8")
            entity.setContentEncoding("UTF-8")
            entity.setContentType("application/json")
            post.setEntity(entity)
        }
        if(headers){
            for(Map.Entry<String,Object> entry : headers.entrySet()){
                if(entry.getValue()!=""){
                    post.setHeader(entry.getKey(), entry.getValue() as String)
                }
            }
        }
        return http(post)
    }

    def http(HttpUriRequest client){
        def res = [:]
        try {
            HttpResponse httpResponse = createDefault().execute(client)
            def code = httpResponse.getStatusLine().getStatusCode()
            def cookies = httpResponse.getHeaders("Set-Cookie")
            if(cookies.size() > 0){
                String cookie = cookies[0]
                res.cookie = cookie.replace("Set-Cookie: ", "").split(";")[0]
            }
            HttpEntity httpEntity = httpResponse.getEntity()
            res.code = code
            res.entity = EntityUtils.toString(httpEntity)//取出应答字符串
        } catch (UnsupportedEncodingException e) {
            log.error( e.getMessage().toString())
            res.code = 501
            res.entity = e.getMessage()
        } catch (ClientProtocolException e) {
            log.error(e.getMessage().toString())
            res.code = 502
            res.entity = e.getMessage()
        } catch (IOException e) {
            log.error(e.getMessage().toString())
            res.code = 503
            res.entity = e.getMessage()
        }
        return res
    }

    CloseableHttpClient createDefault() {
        return HttpClientBuilder.create().build()
    }



}
