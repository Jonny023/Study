build.gradle依赖：
```
//报表依赖
compile "net.sf.jasperreports:jasperreports:5.6.1"
//compile "com.lowagie:itext:2.1.7.js2"
compile "com.lowagie:itext:2.1.7"
compile 'org.olap4j:olap4j:1.2.0'
//此包来自ireport设计器下面
compile files("libs/iTextAsian.jar")
```


直接不用再网页上显示，调用本地打印机打印，只需添加这句：
```
//直接调用本地打印机打印
JasperPrintManager.printReport(jasperPrint, false);
```

```
package com.report

import com.status.ReportExportMode
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JRExporter
import net.sf.jasperreports.engine.JRExporterParameter
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperPrintManager
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRHtmlExporter
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.JRRtfExporter
import net.sf.jasperreports.engine.export.JRXlsExporter
import net.sf.jasperreports.engine.export.JRXlsExporterParameter
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
     * @param reportId 模板名称，不带后缀
     * @param exportMode 导出格式：PDF/EXCEL/RTF/WORD/HTML
     * @param parameterMap map数据
     * @param dataList 模板集合数据
     * @param downloadFileName 下载的文件名
     * @throws Exception
     */
    public static void exportReport(HttpServletRequest request, HttpServletResponse response, String reportId,
                                    String exportMode, Map parameterMap, List dataList, String downloadFileName) throws Exception {
        try {
            ServletContext servletContext = request.getSession().getServletContext()
            File jasperFile = new File(servletContext.getRealPath(File.separator + "templates" + File.separator + "jasper" + File.separator + reportId + ".jasper"))
            if (!jasperFile.exists()) {
                String tempPath = servletContext.getRealPath(File.separator + "templates" + File.separator + "jrxml" + File.separator + reportId + ".jrxml")
                //编译后的.jasper文件存放路径
                String jrxmlDestSourcePath = servletContext.getRealPath(File.separator + "templates") + File.separator + "jasper" + File.separator + reportId + ".jasper"
                JasperCompileManager.compileReportToFile(tempPath, jrxmlDestSourcePath)
            }

            if (parameterMap == null) {
                parameterMap = new HashMap()
            }
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile)
            JasperPrint jasperPrint = null
            JRDataSource source = new JRBeanCollectionDataSource(dataList)
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, source)

            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)
                downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1")// firefox浏览器
            else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)
                downloadFileName = new String(downloadFileName.getBytes("gb2312"), "ISO8859-1")// IE浏览器
            else
                downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1")// firefox浏览器

            if (ReportExportMode.EXP_PDF_MODE.equalsIgnoreCase(exportMode)) {
                exportPdf(response, jasperPrint, downloadFileName)
            } else if (ReportExportMode.EXP_EXCEL_MODE.equalsIgnoreCase(exportMode)) {
                exportExcel(response, jasperPrint, downloadFileName)
            } else if ("WORD".equals(exportMode)) {
                exportWord(response, jasperPrint, downloadFileName)
            } else if ("RTF".equals(exportMode)) {
                exportRTF(response, jasperPrint, downloadFileName)
            } else if ("HTML".equals(exportMode)) {
                exportHtml(response, jasperPrint, downloadFileName)
            }
        } finally {

        }
    }

    /**
     * pdf导出
     */
    private static void exportPdf(HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRPdfExporter exporter = new JRPdfExporter()
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream)

            //屏蔽copy功能
            //exporter.setParameter(JRPdfExporterParameter.,Boolean.TRUE)

            response.setContentType("application/pdf;charset=utf-8")
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".pdf")
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
    private static void exportExcel(HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRXlsExporter exporter = new JRXlsExporter()
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, ouputStream)
            response.setContentType("application/vnd.ms-excel;charset=utf-8")
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".xls")
            // 删除记录最下面的空行
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE)
            // 删除多余的ColumnHeader
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE)
            // 禁用白色背景
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE)
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
    private static void exportWord(HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRExporter exporter = new JRRtfExporter()
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, ouputStream)

            response.setContentType("application/msword;charset=utf-8")
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".doc")
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
    private static void exportRTF(HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRExporter exporter = new JRRtfExporter()
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, ouputStream)

            response.setContentType("application/rtf;charset=utf-8")
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".rtf")
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
    private static void exportHtml(HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream ouputStream = response.getOutputStream()

        try {
            JRHtmlExporter exporter = new JRHtmlExporter()


            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream)
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8")
            //默认情况下用的是px,会导致字体缩小
            exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, "pt")
            //移除空行
            exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE)
            //线条不对齐的解决方法
            exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, Boolean.FALSE)
            exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE)
            response.setContentType("text/html;charset=utf-8")
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
