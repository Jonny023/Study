### Spring security oauth2问题



[springcloud参考](https://www.kanzhun.com/jiaocheng/653871.html)



### 1、数据库存储token

* `token`存储设置为`new JdbcTokenStore(dataSource)`后

>  ERROR org.springframework.security.oauth2.provider.endpoint.TokenEndpoint:169 - Handling error: NullPointerException, null java.lang.NullPointerException: null

```bash
org.springframework.security.oauth2.provider.token.store.JdbcTokenStore:208 - Failed to deserialize authentication for 4557b852-35ae-4c79-9bc3-ff7b8437e4aa java.lang.IllegalArgumentException: java.io.StreamCorruptedException: invalid stream header: 5C786163
```

* 问题分析
  * 数据库字段类型不对
  * `byte`类型在`postgresql`中为`bytea`，设置成`varchar`导致反序列化失败



* 权限注解`@PreAuthorize("#oauth2.hasScope('read')")`

### 1、密码不加密

> 继承`WebSecurityConfigurerAdapter`，重写`configure`

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //用户查询
    auth.userDetailsService(userDetailsService)
    .passwordEncoder(NoOpPasswordEncoder.getInstance());

}
```

### 3、密码加密

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //用户查询
    auth.userDetailsService(userDetailsService)
    .passwordEncoder(passwordEncoder);

}
```

### 4、密码模式登陆

> 密码模式要求必须携带`clientId`,而项目后台登陆一般都是只输入`username`,`password`,可以随便定义一个`clientId`和`secret`,因为对于认证服务器来说，每一个`client`都是一个子应用，都需要在认证服务器注册

### 5、启用@PreAuthorize等注解配置

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }
}
```

### 6、注册调用客户端

```java
/**
 *  客户端授权模式
 * @return
 */
@Bean
public ClientCredentialsAccessTokenProvider ClientCredentialsAccessTokenProvider() {
    ClientCredentialsAccessTokenProvider details = new ClientCredentialsAccessTokenProvider();
    return details;
}

/**
 *  密码授权模式
 * @return
 */
@Bean
public ResourceOwnerPasswordAccessTokenProvider ResourceOwnerPasswordAccessTokenProvider() {
    ResourceOwnerPasswordAccessTokenProvider passwordAccessTokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
    return passwordAccessTokenProvider;
}


/**
     *  管理端登陆
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResultDTO<PasswordAccessTokenDTO> login(String username, String password) {
        LoginUser loginUserAfferent = new LoginUser();
        loginUserAfferent.setUsername(username);
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setAccessTokenUri(tokenUrl);
        resourceDetails.setId("auth-server");
        resourceDetails.setClientId("manage");
        resourceDetails.setClientSecret("f72a33f4a6e44380b57a7d089afec018");
        resourceDetails.setUsername(username);
        resourceDetails.setPassword(password);
        OAuth2AccessToken accessToken = null;
        try {
            accessToken = resourceOwnerPasswordAccessTokenProvider.obtainAccessToken(resourceDetails, new DefaultAccessTokenRequest());
        } catch (InternalAuthenticationServiceException e) {
            return ResultDTO.error(ResultEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), ResultEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
        } catch (Exception e) {
            log.error("找不到用户或密码错误");
            return ResultDTO.error(ResultEnum.AUTHENTICATION_FAILED_ERROR.getCode(), ResultEnum.AUTHENTICATION_FAILED_ERROR.getMsg());
        }
        PasswordAccessTokenDTO dto = new PasswordAccessTokenDTO();
        dto.setAccess_token(accessToken.getValue());
        dto.setExpires_in(accessToken.getExpiresIn());
        dto.setScope(accessToken.getScope());
        dto.setRefresh_token(accessToken.getRefreshToken().getValue());
        dto.setToken_type(accessToken.getTokenType());
        if (log.isDebugEnabled()) {
            log.debug("用户登陆：{}", username);
        }
        return ResultDTO.ok(dto);
    }
```

### 7、自定义服务验证权限

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 用户验证服务
 * @author: Jonny
 */
@Slf4j
@Component(value = "userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (log.isDebugEnabled()) {
            log.debug("login user:" + username);
        }

        UserVO user = userService.findByUsername(username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (user != null) {
            // 从数据库获取权限标识
            List<SysPermission> roles = permissionService.findAllByUserBid(user.getBid(), username);
            if (roles != null && roles.size() > 0) {
                for (SysPermission sysRole : roles) {
                    authorities.add(new SimpleGrantedAuthority(sysRole.getEnname()));
                }
            }
        }
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

}
```

### 8、角色权限注解用法

> `@PreAuthorize`需要配置`@EnableGlobalMethodSecurity(prePostEnabled = true)`

```java
String[] permissions = "USER,ROLE_ADMIN".split(",");

// 有ROLE_,可以通过三种方式校验权限：
@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")

// 无ROLE_,只能通过一种方式校验权限：
@PreAuthorize("hasAuthority('USER')")

@PreAuthorize("#oauth2.hasScope('read')")

// 需要启用注解
@EnableGlobalMethodSecurity(securedEnabled = true)
@Secured("ROLE_ADMIN")
```

### 9、401异常配置

```java
package com.oauth2.exception;

import com.api.base.ResultEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 401异常拦截
 */
@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        final Map<String, Object> mapBodyException = new HashMap<>();

        mapBodyException.put("code", ResultEnum.NEED_CERTIFICATION.getCode());
        mapBodyException.put("msg", ResultEnum.NEED_CERTIFICATION.getMsg());
        mapBodyException.put("path", request.getServletPath());
        mapBodyException.put("timestamp", (new Date()).getTime());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), mapBodyException);
    }
}
```



### 10、资源服务器配置resourceId后，调用时的resourceId必须和配置的id一致

```java
package com.config;

import com.oauth2.exception.AuthExceptionEntryPoint;
import com.oauth2.token.MyAuthenticationKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 配置资源服务器
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "rest-service";

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Resource
    private DataSource dataSource;

    @Resource
    private AuthExceptionEntryPoint oauthEntryPoint;

    @Bean
    public TokenStore tokenStore(){
//        return new RedisTokenStore(redisConnectionFactory);
//        return new JdbcTokenStores(dataSource);
//        return new JwtTokenStore(accessTokenConverter());

        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //自定义了jackson的序列化策略
        //redisTokenStore.setSerializationStrategy(new Oauth2JsonSerializationStrategy());
        //redis key前缀
        redisTokenStore.setPrefix(Const.ADS_BURIED_POINT_TOKEN);

        // 自定义token生成规则，允许多端登陆
        redisTokenStore.setAuthenticationKeyGenerator(new MyAuthenticationKeyGenerator());
        return redisTokenStore;
    }

//    @Bean(name = "tokenStore")
//    @Primary
//    public TokenStore jdbcTokenStore() {
//        return new MyJdbcTokenStore(dataSource);
//    }

    /**
     * 资源服务器认证的配置：
     * 1、设置资源服务器的标识，从配置文件中读取自定义资源名称
     * 2、设置Access Token的数据源(默认内存中)，本项目使用 redis，所以需要配置
     * @param resources
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore()).resourceId(RESOURCE_ID).stateless(true);
        resources.authenticationEntryPoint(oauthEntryPoint);
    }


}
```

### 11、token隔离【密码模式和客户端模式密码分开存】

```java
package com.oauth2.token;

import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @description:
 */
public class MyAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {

    private static final String SCOPE = "scope";

    private static final String USERNAME = "username";

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        if (!authentication.isClientOnly()) {
            values.put(USERNAME, authentication.getName());
        }
        if (authorizationRequest.getScope() != null) {
            values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<String>(authorizationRequest.getScope())));
        }

        // 如果是多租户系统，这里要区分租户ID 条件
        return generateKey(values);
    }
}
```

### 12、登陆调用方法

```java
@Value(value = "${security.oauth2.url}")
private String tokenUrl;
@Resource
private ClientCredentialsAccessTokenProvider clientCredentialsAccessTokenProvider;
@Resource
private ResourceOwnerPasswordAccessTokenProvider resourceOwnerPasswordAccessTokenProvider;

/**
*  管理端登陆
* @param username
* @param password
* @return
*/
@Override
public ResultDTO<PasswordAccessTokenDTO> login(String username, String password) {
    LoginUser loginUserAfferent = new LoginUser();
    loginUserAfferent.setUsername(username);
    ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
    resourceDetails.setAccessTokenUri(tokenUrl);
    resourceDetails.setId("rest-service"); // 必须和资源服务器配置的resourceId一致
    resourceDetails.setClientId("rest-client");
    resourceDetails.setClientSecret("123456");
    resourceDetails.setUsername(username);
    resourceDetails.setPassword(password);
    OAuth2AccessToken accessToken = null;
    try {
        accessToken = resourceOwnerPasswordAccessTokenProvider.obtainAccessToken(resourceDetails, new DefaultAccessTokenRequest());
    } catch (InternalAuthenticationServiceException e) {
        return ResultDTO.error(ResultEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), ResultEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
    } catch (Exception e) {
        log.error("找不到用户或密码错误, msg={}", e);
        return ResultDTO.error(ResultEnum.AUTHENTICATION_FAILED_ERROR.getCode(), ResultEnum.AUTHENTICATION_FAILED_ERROR.getMsg());
    }
    PasswordAccessTokenDTO dto = new PasswordAccessTokenDTO();
    dto.setAccess_token(accessToken.getValue());
    dto.setExpires_in(accessToken.getExpiresIn());
    dto.setScope(accessToken.getScope());
    dto.setRefresh_token(accessToken.getRefreshToken().getValue());
    dto.setToken_type(accessToken.getTokenType());
    if (log.isDebugEnabled()) {
        log.debug("用户登陆：{}", username);
    }
    return ResultDTO.ok(dto);
}
```

