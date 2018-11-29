### 在`build.gradle`中引入依赖

```groovy
// 腾讯云oss
compile ('com.qcloud:cos_api:5.4.9') {
	exclude group: 'org.slf4j', module: 'slf4j-log4j12'
}
```



### 在`grails-app/conf`下创建一个`application-oss.yml`文件

```yaml
oss:
  secretId: AKI**************************H7Ti
  secretKey: FN**************************Ns
  regionName: ap-chengdu
  bucket: img-555231231
  accessAddr: http://www.baidu.com # 访问地址
```



### 然后在`application.yml`中加入

```yaml
spring:
    profiles:
        include: oss
```



### 新建一个`TencentConfig`的`JavaBean`类

* 可以直接在`grails-app/utils`下，也可以在`src/main/java`下

```java
package first.blood.cn.tools.oss.tencent;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云oss初始化配置
 * @Author Lee
 * @Description
 * @Date 2018年11月18日 18:51
 */
@Configuration
@ConfigurationProperties(prefix = "oss")
public class TencentConfig {

    private String secretId;

    private String secretKey;

    private String regionName;

    private String bucket;

    //访问时的域名
    private String accessAddr;

    public String getAccessAddr() {
        return accessAddr;
    }

    public void setAccessAddr(String accessAddr) {
        this.accessAddr = accessAddr;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
```



### 接下来创建一个文件上传的配置类（工具类）`TencentOss`

* 与前面的`TencentConfig`放在一个包下

```java
package first.blood.cn.tools.oss.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author Lee
 * @Description
 * @Date 2018年11月18日 18:35
 */
@Component
public class TencentOss {

    @Autowired
    private TencentConfig tc;

    /**
     * 上传至腾讯云COS
     *
     * @param inputStream 输入流
     * @param fileName    保存路径+文件名：/2018/11/21/demo.png
     * @param contentType 文件类型，默认是 application/octet-stream
     * @return
     */
    public String upload(InputStream inputStream, String fileName) throws IOException {

        COSClient cosClient = null;

        try {

            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(tc.getSecretId(), tc.getSecretKey());
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            // clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
            ClientConfig clientConfig = new ClientConfig(new Region(tc.getRegionName()));
            // 配置使用https
            // 3 生成cos客户端
            cosClient = new COSClient(cred, clientConfig);
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String bucketName = tc.getBucket();

            ObjectMetadata objectMetadata = new ObjectMetadata();

            PutObjectResult putObjectResult = cosClient.putObject(bucketName, fileName, inputStream, objectMetadata);

            String etag = putObjectResult.getETag();

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
        return tc.getAccessAddr() + "/" + fileName;
    }
}
```



### 然后在程序入口类上加上`@ComponetScan`注解

```java
package first.blood

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.context.annotation.ComponentScan

@ComponentScan
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}
```



### 控制器方法

```groovy
/**
 *  单文件上传
 * @return
 */
def imgFile() {

    def map = [success: 0, message: "上传失败"]
    def file = null
    def inputStream = null
    try {

        file = params["editormd-image-file"]
        inputStream = file.getInputStream()

        String fileName = file.getOriginalFilename()

        if (!file && !fileName) {
            map.message = "请选择图片文件"
            render map as JSON
            return
        }

        // 判断文件大小，MAX_POST_SIZE自己定义
        if (file.getSize() > MAX_POST_SIZE) {
            map.message = "只能上传不超过2M的图片文件"
            render map as JSON
            return
        }

        // 判断是否是图片文件
        if (!ImgUtils.isImage(file.getInputStream())) {
            map.message = "只允许上传图片文件"
            render map as JSON
            return
        }

        String prefix = fileName.substring(fileName.lastIndexOf("."))
        // 新文件名，这里调用Date工具类生成目录结构2018/12/30
        String newFileName = DateUtils.getUrl() + "/" + UUIDUtils.id() + prefix

        String accessAddr = oss.upload(inputStream,newFileName)
        map.success = 1
        map.message = "上传成功"
        map.url = accessAddr
    } catch (e) {
        log.error("文件上传失败，msg={}", e)
        map.message = "请检查参数是否正确，只能上传不超过2M的图片文件"
    }
    render map as JSON
}
```



### 踩坑注意事项

* 文件流只能用一次
  * 前面定义了`inputStream`在中途用过一次就会出问题，上传的文件只有几B大小，有的是0B