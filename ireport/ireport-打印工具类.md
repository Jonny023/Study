# 代码

## 完整依赖

```
buildscript {
    ext {
        grailsVersion = project.grailsVersion
    }
    repositories {
        mavenLocal()
        maven { url "https://repo.grails.org/grails/core" }
    }
    dependencies {
        classpath "org.grails:grails-gradle-plugin:$grailsVersion"
        classpath "com.bertramlabs.plugins:asset-pipeline-gradle:2.8.2"
        classpath "org.grails.plugins:hibernate4:5.0.5"
    }
}

group "talentExchange"

apply plugin: "eclipse"
apply plugin: "idea"
apply plugin: "war"
apply plugin: "org.grails.grails-web"
apply plugin: "org.grails.grails-gsp"
apply plugin: "asset-pipeline"

ext {
    grailsVersion = project.grailsVersion
    gradleWrapperVersion = project.gradleWrapperVersion
}

//配置全局仓库
allprojects {
    repositories {
        mavenLocal()
        maven { url "https://repo.huaweicloud.com/repository/maven/" }
        maven { url "http://maven.aliyun.com/nexus/content/groups/public" }
        maven { url "https://repo.grails.org/grails/core" }
        maven { url "http://jasperreports.sourceforge.net/maven2/" }
    }
}

dependencyManagement {
    imports {
        mavenBom "org.grails:grails-bom:$grailsVersion"
    }
    applyMavenExclusions false
}


configurations.all {
    resolutionStrategy {
        force 'commons-io:commons-io:2.4'
        force 'org.apache.commons:commons-lang3:3.4'
    }
}

dependencies {
    compile "org.springframework.boot:spring-boot-starter-logging"
    compile "org.springframework.boot:spring-boot-autoconfigure"
    compile "org.grails:grails-core"
    compile "org.springframework.boot:spring-boot-starter-actuator"
    provided "org.springframework.boot:spring-boot-starter-tomcat"
    compile "org.grails:grails-dependencies"
    compile "org.grails:grails-web-boot"
    compile "org.grails.plugins:cache"
    compile "org.grails.plugins:scaffolding"
    compile "org.grails.plugins:hibernate4"
    compile "org.hibernate:hibernate-ehcache"

    //mysql
    compile group: 'mysql', name: 'mysql-connector-java', version: '5.1.38'

    //权限
    compile "org.grails.plugins:spring-security-core:3.1.0"

    //邮件服务
    compile 'org.grails.plugins:mail:2.0.0.RC6'

    //http请求
    compile group: 'org.apache.httpcomponents', name: 'httpclient', version: '4.3.5'
    compile group: 'org.apache.httpcomponents', name: 'httpcore', version: '4.3.3'
    compile group: 'com.alibaba', name: 'fastjson', version: '1.2.38'

    //freemarker、pdf、doc、xml、excel
//    compile 'itext:itext:2.0.8'
    compile 'com.itextpdf:itext-asian:5.2.0'
    compile 'org.xhtmlrenderer:core-renderer:8.0'
    compile 'freemarker:freemarker:2.3.9'
    compile files('libs/pd4ml_demo.jar')

    //解析excel
    compile 'org.grails.plugins:excel-import:3.0.0.RC4'

    compile "com.oracle:ojdbc6:11.2.0.3"

    console "org.grails:grails-console"
    profile "org.grails.profiles:web:3.1.5"
    runtime "com.bertramlabs.plugins:asset-pipeline-grails:2.8.2"
//    runtime "com.h2database:h2"
    testCompile "org.grails:grails-plugin-testing"
    testCompile "org.grails.plugins:geb"
    testRuntime "org.seleniumhq.selenium:selenium-htmlunit-driver:2.47.1"
    testRuntime "net.sourceforge.htmlunit:htmlunit:2.18"

    //流程
    compile 'org.activiti:activiti-engine:5.17.0'
    compile 'org.activiti:activiti-bpmn-layout:5.16.2'
    compile 'org.activiti:activiti-spring:5.17.0'
    compile 'org.activiti:activiti-json-converter:5.17.0'

    //ueditor
    compile files("libs/json.jar")

    //报表依赖
    compile "net.sf.jasperreports:jasperreports:5.6.1"
    compile "com.lowagie:itext:2.1.7"
    compile 'org.olap4j:olap4j:1.2.0'
    compile files("libs/iTextAsian.jar")

    //word 转换 html
    compile 'org.apache.poi:poi-scratchpad:3.10-FINAL'
    compile "org.apache.poi:poi:3.12"
    compile "org.apache.poi:poi-scratchpad:3.12"
    compile "org.apache.poi:poi-ooxml:3.12"
    compile "org.apache.poi:poi-ooxml-schemas:3.12"
    compile "org.apache.poi:ooxml-schemas:1.3"
    compile "fr.opensagres.xdocreport:xdocreport:1.0.6"

    //定时器
    compile 'org.grails.plugins:quartz:2.0.0.M4'

    //xss插件
    compile 'org.jsoup:jsoup:1.11.2'
    //fastjson
    compile group: 'com.alibaba', name: 'fastjson', version: '1.2.44'

    //提取中文拼音首字母
    compile group: 'com.belerweb', name: 'pinyin4j', version: '2.5.0'

    //获取图片上传经纬度
//    compile 'com.drewnoakes:metadata-extractor:2.6.2'
    compile 'com.drewnoakes:metadata-extractor:2.11.0'

    //工行依赖
    compile files("libs/icbc.jar")
    compile files("libs/InfosecCrypto_Java1_02_JDK14+.jar")

}

task wrapper(type: Wrapper) {
    gradleVersion = gradleWrapperVersion
}

assets {
    minifyJs = true
    minifyCss = true
}

//启动时加载
bootRun {
    systemProperties = System.properties
}

//编译java文件时用utf-8编码
//tasks.withType(JavaCompile) {
//    options.encoding = "UTF-8"
//}

//解决编译java时警告：请使用 -Xlint:unchecked 重新编译
//compileJava {
//    options.compilerArgs << "-Xlint:unchecked"
//}

[compileJava, compileTestJava]*.options*.encoding = 'UTF-8'

//解决运行时提示path文件名过长的问题
ext {
    //overcome error executing java for gson views compilation
    grails {
        pathingJar = true
    }
}

```

> 代码
```groovy
package com.report

import com.common.file.FileBrowserUtil
import com.status.ReportExportMode
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.*
import net.sf.jasperreports.engine.util.JRLoader

import javax.servlet.ServletContext
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExpeortReportService {

    /**
     *  获取打印报表
     * @param request
     * @param response
     * @param reportId      模板名称，不带后缀
     * @param exportMode    导出格式：PDF/EXCEL/RTF/WORD/HTML
     * @param parameterMap  map数据
     * @param dataList      模板集合数据
     * @param downloadFileName 下载的文件名
     * @throws Exception
     */
    static void exportReport(HttpServletRequest request, HttpServletResponse response, String reportId,
                                    String exportMode, Map parameterMap, List dataList, String downloadFileName) throws Exception {
        try {
            ServletContext servletContext = request.getSession().getServletContext()
            File jasperFile = new File(servletContext.getRealPath(File.separator+"templates"+File.separator+"jasper"+ File.separator + reportId + ".jasper"))
            if (!jasperFile.exists()) {
                String tempPath = servletContext.getRealPath(File.separator+"templates"+File.separator+"jrxml"+File.separator + reportId+".jrxml")
                //编译后的.jasper文件存放路径
                JasperCompileManager.compileReportToFile(tempPath,jasperFile.getAbsolutePath())
            }

            if (parameterMap == null) {
                parameterMap = new HashMap()
            }
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile)
            JasperPrint jasperPrint = null
            JRDataSource source = new JRBeanCollectionDataSource(dataList)
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, source)

//            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
//                downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1")// firefox浏览器
//            } else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
//                downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO8859-1")// IE浏览器
//            } else {
//                downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1")// firefox浏览器
//            }
            if (ReportExportMode.EXP_PDF_MODE.equalsIgnoreCase(exportMode)) {
                exportPdf(request,response, jasperPrint, downloadFileName)
            } else if (ReportExportMode.EXP_EXCEL_MODE.equalsIgnoreCase(exportMode)) {
                exportExcel(request,response, jasperPrint, downloadFileName)
            } else if ("WORD".equals(exportMode)) {
                exportWord(request,response, jasperPrint, downloadFileName)
            } else if ("RTF".equals(exportMode)) {
                exportRTF(request,response, jasperPrint, downloadFileName)
            } else if ("HTML".equals(exportMode)) {
                exportHtml(request,response, jasperPrint, downloadFileName)
            }else if("PRINT".equals(exportMode)) {
                exporPrint(request,response, jasperPrint, downloadFileName)
            }
        } finally {

        }
    }

    /**
     * pdf导出
     */
    private static void exportPdf(HttpServletRequest request,HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRPdfExporter exporter = new JRPdfExporter()
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream)

            //屏蔽copy功能
//            exporter.setParameter(JRPdfExporterParameter.,Boolean.TRUE)

            response.setContentType("application/pdf;charset=utf-8")
//            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".pdf")
            String disposition = FileBrowserUtil.getContentDisposition(downloadFileName+".pdf",request);
            response.setHeader("Content-Disposition", disposition)
            response.setHeader("Connection", "close");
            response.setCharacterEncoding("utf-8")
            exporter.exportReport()
            ouputStream.flush()
        } finally {
            try {
                ouputStream.close()
            } catch (Exception e) {
            }
        }
    }

    /**
     * excel导出
     */
    private static void exportExcel(HttpServletRequest request,HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRXlsExporter exporter = new JRXlsExporter()
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, ouputStream)
            response.setContentType("application/vnd.ms-excel;charset=utf-8")
//            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".xls")
            response.setHeader("Content-Disposition", FileBrowserUtil.getContentDisposition(downloadFileName+".xls",request))
            response.setHeader("Connection", "close");
            // 删除记录最下面的空行
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE)
            // 删除多余的ColumnHeader
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE)
            //禁用白色背景
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE)
            exporter.exportReport()
            ouputStream.flush()
        } finally {
            try {
                ouputStream.close()
            } catch (Exception e) {
            }
        }
    }

    /**
     * 导出word
     */
    private static void exportWord(HttpServletRequest request,HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRExporter exporter = new JRRtfExporter()
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, ouputStream)

            response.setContentType("application/msword;charset=utf-8")
//            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".doc")
            response.setHeader("Content-Disposition", FileBrowserUtil.getContentDisposition(downloadFileName+".doc",request))
            response.setHeader("Connection", "close");
            exporter.exportReport()
            ouputStream.flush()
        } finally {
            try {
                ouputStream.close()
            } catch (Exception e) {
            }
        }
    }

    /**
     * 导出RTF
     */
    private static void exportRTF(HttpServletRequest request,HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRExporter exporter = new JRRtfExporter()
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, ouputStream)

            response.setContentType("application/rtf;charset=utf-8")
//            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".rtf")
            response.setHeader("Content-Disposition", FileBrowserUtil.getContentDisposition(downloadFileName+".pdf",request))
            response.setHeader("Connection", "close");
            exporter.exportReport()
            ouputStream.flush()
        } finally {
            try {
                ouputStream.close()
            } catch (Exception e) {
            }
        }
    }

    /**
     * 导出html
     */
    private static void exportHtml(HttpServletRequest request,HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRHtmlExporter exporter = new JRHtmlExporter()


            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream)
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8")
            //默认情况下用的是px,会导致字体缩小
            exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT,"pt")
            //移除空行
            exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE)
            //线条不对齐的解决方法
            exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES,Boolean.FALSE)
            exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE)
            response.setContentType("text/html;charset=utf-8")
            response.setHeader("Connection", "close");
            exporter.exportReport()
            ouputStream.flush()
        } finally {
            try {
                ouputStream.close()
            } catch (e) {
            }
        }
    }

    /**
     * 打印报表
     * @param response
     * @param jasperPrint
     * @param downloadFileName
     * @throws JRException
     * @throws IOException
     */
    private static void exporPrint(HttpServletRequest request,HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRHtmlExporter exporter = new JRHtmlExporter()


            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream)
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8")
            //默认情况下用的是px,会导致字体缩小
            exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT,"pt")
            //移除空行
            exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE)
            //线条不对齐的解决方法
            exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES,Boolean.FALSE)
            exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE)
            response.setContentType("text/html;charset=utf-8")
            response.setHeader("Connection", "close");

            //直接调用本地打印机打印
            JasperPrintManager.printReport(jasperPrint, false)
            exporter.exportReport()
            ouputStream.flush()
        } finally {
            try {
                ouputStream.close()
            } catch (e) {
            }
        }
    }
}

```
