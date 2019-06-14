## Grails3 Spring Secuirty自定义加密方式

#### 应用场景

> 公司老项目使用`grails2.0+`版本，他的加密方式为`encodeAsSHA256`，数据是通过导入实现，要兼容以前数据加密方式，使以前使用老项目的用户也能用原先的密码登录。

* 首先，我做了一下测试

```groovy
def test() {
    map.password1 = "123456".encodeAsSHA256()
    map.password2 = springSecurityService.encodePassword("123456")
    render map as JSON
}
```

* 结果发现他们是两种不同的加密方式



## 接下来只能通过重写父类方法实现统一加密

* 第一步：自定义一个类，我习惯在`src/main/goovy`下创建`CustomPasswordEncoder.groovy`类，也可以创建`java`类

```groovy
package com.encoder

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder
import org.springframework.security.authentication.encoding.PasswordEncoderUtils
import org.springframework.security.crypto.codec.Hex
import org.springframework.util.Assert

import java.security.MessageDigest

/**
 *  自定义加密覆盖默认加密方式
 *  项目spring-security版本为3.1.0，本可以重新BaseDigestPasswordEncoder类
 *  但是本人看BaseDigestPasswordEncoder类被标记为删除了，所以通过重写MessageDigestPasswordEncoder类方法实现
 */
class CustomPasswordEncoder extends MessageDigestPasswordEncoder {

    // 默认为MD5
    private String algorithm = "MD5";

    // 加密次数（提高安全）
    private int iterations = 1;

    CustomPasswordEncoder() {
        // 当前类默认构造器，因为父类没有空构造器，所以这里必须调用父类有参构造，这里传入参数必须是父类存在的加密规则，否则报错
        super("SHA-256")
    }

    CustomPasswordEncoder(String algorithm) {
        super(algorithm, false);
        this.algorithm = algorithm
    }

    CustomPasswordEncoder(String algorithm, boolean encodeHashAsBase64) throws IllegalArgumentException {
        super()
        setEncodeHashAsBase64(encodeHashAsBase64);
        this.algorithm = algorithm;
        getMessageDigest();
    }

    @Override
    String encodePassword(String rawPass, Object salt) {
        String saltedPass = this.mergePasswordAndSalt(rawPass, salt, false)
        MessageDigest messageDigest = this.getMessageDigest()
        byte[] digest = messageDigest.digest(saltedPass.getBytes("UTF-8"))
        for (int i = 1; i < iterations; i++) {
            digest = messageDigest.digest(digest);
        }
        // 先判断是否启用base64
        if (this.getEncodeHashAsBase64()) {
            return new String(Base64.encodeAsBase64(digest))
        // 判断是否为自定义的SHA-256-1(框架自定加密方式，非spring security框架，这里指的是grails自带的加密)
        } else if ("SHA-256-1".equalsIgnoreCase(algorithm)) {
            return rawPass.encodeAsSHA256()
        } else {
            // 使用用户配置的其他加密方式
            return new String(Hex.encode(digest))
        }
    }

    @Override
    boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String pass1 = "" + encPass
        String pass2 = encodePassword(rawPass, salt)
        return PasswordEncoderUtils.equals(pass1, pass2)
    }

    String getAlgorithm() {
        return algorithm;
    }

    void setIterations(int iterations) {
        Assert.isTrue(iterations > 0, "Iterations value must be greater than zero");
        this.iterations = iterations;
    }
}
```

* 第二步：在`grails-app/conf/spring/resources.groovy`中注册一下`bean`

```groovy
beans = {

    // 自定义密码
    passwordEncoder(com.encoder.CustomPasswordEncoder) {
        encodeHashAsBase64 = false // 若为true，则以base64方式加密
    }

}
```

* 第三步：在`grails-app/conf/application.groovy`中添加配置

```groovy
// 原框架加密方式，有SHA-256、bcrypt、MD5、pbkdf2，默认为bcrypt
// 自定义加密默认为MD5，这里SHA-256-1为自定义加密，还可以用SHA-256等
grails.plugin.springsecurity.password.algoritham = 'SHA-256-1'
```



## 到这里配置就已经完成了，启动测试



# 注意：项目中用到`domain.save(failOnError: true)`的有时需要修改为`domain.save(flush: true)`，我测试密码修改时，`failOnError: true`没有修改成功。