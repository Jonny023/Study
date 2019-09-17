# jxls入门

* 依赖

```groovy
compile 'org.jxls:jxls:2.6.0'
compile 'org.jxls:jxls-poi:2.6.0-rc1'
compile 'org.jxls:jxls-jexcel:1.0.9'
```

* 服务
  * `try-with-resources`不兼容`groovy`语法，所以只能用`withCloseable`或`withStream`等代替

```groovy
package com.jonny

import org.jxls.common.Context
import org.jxls.transform.poi.PoiContext
import org.jxls.util.JxlsHelper

import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
/**
* @author Jonny
* @description excel公用服务类
* @date 2019/09/16
*/
class ExcelToolService {

    /**
     *  生成excel文件
     * @param request
     * @param datas 数据集合
     * @param template 模板文件：annex.xls，存放到resources下
     * @param fileName 生成后的文件名，不带后缀
     * @return
     */
    def create(HttpServletRequest request, datas, String template, String fileName) {

        ServletContext servletContext = request.getSession().getServletContext()
        String path = servletContext.getRealPath("/excelTemp")
        File fileDir = new File(path)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }

        String filePath = path + File.separator + fileName + ".xls"
        File file = new File(filePath)
        if (!file.exists()) {
            file.createNewFile()
        }

        new FileInputStream(servletContext.getRealPath("/excelTemplates/") + template).withCloseable { inputStream ->
            new FileOutputStream(filePath).withCloseable { os ->
                Context context = new PoiContext()
                context.putVar("datas", datas)
                JxlsHelper.getInstance().processTemplate(inputStream, os, context)
            }
        }
    }
}
```

* 模板文件编写
  * 模板`xls`文件单元格，按`SHIFT+F2`添加批注
  * `jx:area(lastCell="C3")` - 定义数据范围
  * `jx:each(items="datas" var="data" lastCell="C3")` - 遍历数据集合
  * `${${data.xx}}` - 输出指定的值

| jx:area(lastCell="C3")                          |             |             |
| ----------------------------------------------- | ----------- | ----------- |
| jx:each(items="datas" var="data" lastCell="C3") |             |             |
| ${data.xx}                                      | ${data.xxx} | ${data.xxx} |
|                                                 |             |             |

### 超链接

```html
${util.hyperlink(data.url,'前往')}
```
