# 拦截器资源鉴权

```java
package cn.com.interceptor;

import cn.com.dao.jpa.entity.SysSite;
import cn.com.service.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断请求hm.js的来源域名和si注册到系统中的域名是否匹配
 */
@Component
@Slf4j
public class JsInterceptor implements HandlerInterceptor {

    @Resource
    private SiteService siteService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String si = request.getParameter("si");
        String referer = request.getHeader("Referer");
        if (StringUtils.isBlank(si) || StringUtils.isBlank(referer)) {
            log.warn("非法请求hm.js，si:{},referer:{}", si, referer);
            return false;
        }
        SysSite sysSite = siteService.queryBySi(si);
        if (sysSite == null) {
            log.warn("非法请求hm.js,si未注册,referer:{},si:{}", referer, si);
            return false;
        }
        String domain = sysSite.getDomain();
        if (StringUtils.isBlank(domain)) {
            log.warn("非法请求hm.js,si未注册域名,referer:{},si:{}", referer, si);
            return false;
        }
        domain = domain.replace("http://", "").replace("https://", "").replace("www.", "");
        boolean result = referer.contains(domain);
        if (!result) {
            log.warn("非法请求hm.js,si注册域名与请求来源不一致,referer:{},si:{}", referer, si);
            return false;
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
```

