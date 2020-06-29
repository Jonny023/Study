# 例

```java
package com.gd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class MainApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(MainApplicationTests.class);

    public static void main(String[] args) {
        test();
    }

    /**
     *  携带token请求
     */
    public static void test() {
        //headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("x-token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTM5NTA3NTUsInVzZXJuYW1lIjoiMTMxMjM0NTY3ODgifQ.Jc1W38fdg1P3kQVjcBbVBh4xjQ6gQJqWn5INWJfGmXM");
        //body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("companyId", "7ddfa3949dcb49fb83c11525e055771e");
        //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        //post
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://192.168.1.8:8081/role/role_list", requestEntity, String.class);
        System.out.println(responseEntity.getBody());

        log.error("状态： {}", responseEntity.getStatusCode());
        log.error("状态码： {}", responseEntity.getStatusCodeValue());
        log.error("请求头： {}", responseEntity.getHeaders());

    }
}

```
