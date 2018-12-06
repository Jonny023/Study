### 创建Filter

```groovy
package com.context

import grails.core.GrailsApplication
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.context.ApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
@Slf4j
class ContextFilter implements Filter {

    private ApplicationContext applicationContext
    private GrailsApplication grailsApplication

    void init(FilterConfig fc) throws ServletException {
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(fc.servletContext)
        grailsApplication = applicationContext.getBean(GrailsApplication)
    }

    void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req
        HttpServletResponse response = (HttpServletResponse) res

        if ('GET' == request.method) {
            doGet request, response
        } else {
            // assume POST
            doPost request, response
        }
    }

    void destroy() {}

    private void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.writer.write html(request.contextPath)
    }

    private void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis()

        response.setCharacterEncoding("UTF-8")
        response.setContentType("text/html;charset=utf-8")
        String code = request.getParameter('code')

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        PrintStream out = new PrintStream(baos)
        PrintStream systemOut = System.out

        Throwable e
        String result = ''
        try {
            System.out = out
            result = new GroovyShell(grailsApplication.classLoader, new Binding(
                    config: grailsApplication.config,
                    ctx: applicationContext,
                    grailsApplication: grailsApplication,
                    out: out,
                    request: request,
                    session: request.session)).evaluate(code)
        }
        catch (Throwable t) {
            e = t
        }
        finally {
            System.out = systemOut
        }

        if (e) {
            StringWriter sw = new StringWriter()
            e.printStackTrace new PrintWriter(sw)
            result = sw.toString().replace('\t', '   ').replace(System.getProperty('line.separator'), '<br/>\n')
        }

        response.writer << html(request.contextPath, code, """\
        耗时: ${System.currentTimeMillis() - startTime}ms
        
        输出:
        \t${baos.toString('UTF8')}

        ${e ? '异常' : '结果'}:
        \t$result""")
    }

    private String html(String contextPath, String code = '', String results = '') {
        """
        <html>
        <head>
        <title>Hack</title>
        </head>
        <body>
           <form action="$contextPath/hack" method="POST">
              <span>Code: (binding vars include <i>config</i>, <i>ctx</i>, <i>grailsApplication</i>, <i>out</i>, <i>request</i>, <i>session</i>)</span><br/>
              <textarea name="code" cols="120" rows="25">$code</textarea><br/>
              <input type="submit" value="Execute" name="execute" /><br/>
              <span>Results:</span><br/>
              <textarea name="results" cols="120" rows="25" disabled="disabled">$results</textarea>
           </form>
        </body>
        </html>
        """
    }
}


```

### 在resources.groovy中注入

```groovy
contextFilter(ContextFilter)
```

### 访问方式

```bash
http://localhost:808/test/hack
```
