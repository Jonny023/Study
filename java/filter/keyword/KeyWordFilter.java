package com.jeecms.common.web.keyword;

import com.jeecms.cms.entity.assist.CmsSensitivity;
import com.jeecms.cms.manager.assist.CmsSensitivityMng;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class KeyWordFilter implements Filter {

    protected final Logger log = LoggerFactory.getLogger(KeyWordFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        WebApplicationContext cxt = WebApplicationContextUtils.getWebApplicationContext(context);
        if (cxt != null && cxt.getBean("cmsSensitivityMng") != null && cmsSensitivityMng == null)
            cmsSensitivityMng = (CmsSensitivityMng) cxt.getBean("cmsSensitivityMng");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        KeyWordWrapper wrapperResponse = new KeyWordWrapper((HttpServletResponse) response);//转换成代理类
        // 这里只拦截返回，直接让请求过去，如果在请求前有处理，可以在这里处理

        chain.doFilter(request, wrapperResponse);

        byte[] content = wrapperResponse.getResponseData();//获取返回值
        //判断是否有值
        if (content.length > 0) {

            String str = new String(content, "UTF-8");
            String filterStr = "";

            try {
                //......根据需要处理返回值
                filterStr = replaceContent(str);
            } catch (Exception e) {
                log.error("关键词过滤异常 {}", e.getMessage());
            }
            //把返回值输出到客户端
            ServletOutputStream out = response.getOutputStream();
            out.write(filterStr.getBytes());
            out.flush();
            out.close();
        }

    }

    @Override
    public void destroy() {

    }

    protected CmsSensitivityMng cmsSensitivityMng;

    /**
     * 过滤敏感词
     *
     * @param oldContent 原内容
     * @return newContent 处理后内容
     */
    protected String replaceContent(String oldContent) {

        List<CmsSensitivity> lists = cmsSensitivityMng.getListByProcessMode("REPLACE");
        if (lists != null && lists.size() > 0) {
            for (CmsSensitivity keyWord : lists) {
                if (!StringUtils.isBlank(keyWord.getReplacement())) {
                    oldContent = oldContent.replaceAll(keyWord.getSearch(), keyWord.getReplacement());
                }
            }
        }
        return oldContent;
    }
}
