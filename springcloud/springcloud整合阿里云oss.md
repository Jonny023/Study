## springcloud整合阿里云oss

[阿里云](https://aliyun.com)

> 打开**产品》存储》对象存储**，需要进行登录，首次访问需要进行开通和实名认证
>
> 进入**Bucket 列表》帮助文档**

* #### 创建 Bucket

  * 低频访问存储
  * 公共读
  * 开通AccessKey》使用子账户的AccessKey(Open API 调用访问)，并配置权限AliyunOSSFullAccess
  * 跨域配置
    * bucket详情页》权限管理》跨域设置》设置》创建规则：**来源*，允许Method:POST，允许Headers:***

* 文档

  * [Java SDK](https://help.aliyun.com/document_detail/32009.html)
  * [alibaba cloud oss](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-oss-spring-boot-sample)

## 方式1

> 通过后端上传，文件经过服务器，会占用带宽，不推荐

### 1.依赖

```xml
<dependencies>		
		<dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>aliyun-oss-spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>com.aliyun</groupId>
                    <artifactId>aliyun-java-sdk-core</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>com.aliyun</groupId>
            <artifactId>aliyun-java-sdk-core</artifactId>
            <version>4.5.0</version>
        </dependency>
	</dependencies>
	<dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>aliyun-spring-boot-dependencies</artifactId>
                <version>1.0.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```



### 2.yml配置

```yaml
alibaba:
  cloud:
    access-key: xxxx
    secret-key: xxxx
    oss:
      endpoint: oss-cn-chengdu.aliyuncs.com
      bucket: gulimall-023
```

* properties配置

  ```properties
  alibaba.cloud.access-key=
  alibaba.cloud.secret-key=
  alibaba.cloud.oss.endpoint=
  alibaba.cloud.oss.bucket=
  ```

  

### 3.测试

> 后端直接上传

```java
@Service
 public class YourService {
 	@Autowired
 	private OSSClient ossClient;

 	public void saveFile() {
 		// download file to local
 		ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(""));
        if (ossClient != null) {
            ossClient.shutdown();
        }
 	}
 }

@SpringBootTest(classes = GulimallProductApplication.class)
public class Demo {

    @Resource
    private OSSClient ossClient;

    @Test
    public void exe() throws Exception {
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "gulimall-023";
        //// 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "屏幕截图.png";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath = "C:\\Users\\admin\\Pictures\\Screenshots\\屏幕截图(1).png";
        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
```

## 方式2（推荐）

> 直接通过前端上传，服务器提供签名
>
> 最佳实践》网站与移动应用》Web端上传数据至OSS》服务端签名后直传

[服务端签名后由前端直传](https://help.aliyun.com/document_detail/31926.html)

```java
package com.atguigu.gulimall.thirdparty.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.atguigu.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class OssController {

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Resource
    private OSS ossClient;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;
    @Value("${alibaba.cloud.oss.bucket}")
    private String bucket;
    @Value("${alibaba.cloud.access-key}")
    private String accessId;

    @RequestMapping("/oss/policy")
    public R policy() {
        // 填写Bucket名称，例如examplebucket。
        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = String.format("%s%s%s%s", "https://", bucket, ".", endpoint);

        // 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
        //String callbackUrl = "https://192.168.0.0:8888";
        String format = LocalDateTime.now().format(dateTimeFormatter);
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String dir = format + "/";
        Map<String, String> respMap = null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        }
        return R.ok().put("data", respMap);
    }
}
```

