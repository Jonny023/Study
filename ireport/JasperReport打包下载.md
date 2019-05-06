## JasperReport工具类

> 打印pdf，如果数据量过大或导致GC，需要分批处理，生成多个pdf打为zip包下载

* 工具类

```java
package com.common.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *  文件流转换
 */
public enum FileUtil {

    E;

    public ByteArrayOutputStream fileToByteArrayOutPutStream(File file) throws IOException {
        ByteArrayOutputStream out = null;
        FileInputStream inputStream = null;
        try {
            out = new ByteArrayOutputStream();
            inputStream = new FileInputStream(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            out.write(buffer);
        } finally {
            if(inputStream!=null) inputStream.close();
        }
        return out;
    }
}

```


* 服务类

```groovy
package com.report

import com.common.file.FileBrowserUtil
import com.common.file.FileUtil
import com.status.ReportExportMode
import groovy.util.logging.Slf4j
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JRExporter
import net.sf.jasperreports.engine.JRExporterParameter
import net.sf.jasperreports.engine.JRParameter
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
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
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter
import net.sf.jasperreports.engine.util.JRLoader
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value

import javax.servlet.ServletContext
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Slf4j
class ExpeortReportService {

    @Value('${file.abspath}')
    private String path

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
    void exportReport(HttpServletRequest request, HttpServletResponse response, String reportId, String exportMode, Map parameterMap, List dataList, String downloadFileName) throws Exception {
        try {
            ServletContext servletContext = request.getSession().getServletContext()
            File jasperFile = new File(servletContext.getRealPath(File.separator + "templates" + File.separator + "jasper" + File.separator + reportId + ".jasper"))
            if (!jasperFile.exists()) {
                String tempPath = servletContext.getRealPath(File.separator + "templates" + File.separator + "jrxml" + File.separator + reportId + ".jrxml")
                //编译后的.jasper文件存放路径
                JasperCompileManager.compileReportToFile(tempPath, jasperFile.getAbsolutePath())
            }

            if (parameterMap == null) {
                parameterMap = new HashMap()
            }
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile)
            JasperPrint jasperPrint = null
            JRDataSource source = null

            if (ReportExportMode.EXP_PDF_MODE.equalsIgnoreCase(exportMode)) {
                // 大于500条数据打包下载
                if(dataList.size() > 500) {
                    // 切割集合(分组合并【每500条数据分割为一个集合】)
                    def allDatas = dataList.collate(500)
                    String savePath = path + File.separator + "export_pdf" + File.separator + System.currentTimeMillis() + File.separator
                    File dir = new File(savePath)
                    if(!dir.exists()) {
                        dir.mkdirs()
                    }
                    int index = 1
                    for (List list : allDatas) {
                        source = new JRBeanCollectionDataSource(list)
                        jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, source)
                        // 输出pdf到本地
                        JasperExportManager.exportReportToPdfFile(jasperPrint, savePath + "${downloadFileName}(${index}).pdf")
                        index++
                    }
                    // 打包为zip下载
                    exportZip(response, request, downloadFileName, savePath)
                } else {
                    exportPdf(request,response, jasperPrint, downloadFileName)
                }
            } else if (ReportExportMode.EXP_EXCEL_MODE.equalsIgnoreCase(exportMode)) {
                exportExcel(request, response, jasperPrint, downloadFileName)
            } else if ("WORD".equals(exportMode)) {
                exportWord(request, response, jasperPrint, downloadFileName)
            } else if ("RTF".equals(exportMode)) {
                exportRTF(request, response, jasperPrint, downloadFileName)
            } else if ("HTML".equals(exportMode)) {
                exportHtml(request, response, jasperPrint, downloadFileName)
            } else if ("PRINT".equals(exportMode)) {
                exporPrint(request, response, jasperPrint, downloadFileName)
            }
        } finally {

        }
    }

    /**
     *  文件打包为zip
     * @param response
     * @param request
     * @param downloadFileName 现在文件名: xxx
     * @param osList 字节流
     * @param suffix 文件后缀
     */
    private static void exportZip(HttpServletResponse response, HttpServletRequest request, String downloadFileName, String path) {
        OutputStream os = null
        ZipOutputStream zipOut = null
        String fileName = downloadFileName + ".zip"
        File dirs = null
        try {
            os = response.getOutputStream()
            response.setContentType("application/octet-stream;charset=UTF-8")
            response.setHeader("Set-Cookie", "fileDownload=true; path=/")
            String disposition = FileBrowserUtil.getContentDisposition(fileName, request)
            response.setHeader("Content-Disposition", disposition)
            zipOut = new ZipOutputStream(os)
            dirs = new File(path)
            if (dirs.isDirectory()) {
                ByteArrayOutputStream out = null
                dirs.eachFileRecurse { file ->
                    out = FileUtil.E.fileToByteArrayOutPutStream(file)
                    compressFile(out,zipOut,file.getName())
                }
            }
            /**zip写入磁盘**/
            zipOut.flush()
        } catch (Exception e) {
            log.error("报表打包出现错误", e)
        } finally {
            IOUtils.closeQuietly(zipOut)
            dirs.deleteDir()
        }
    }

    /**
     * pdf导出
     */
    private static void exportPdf(HttpServletRequest request, HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream outputStream = response.getOutputStream()

        try {
            JRPdfExporter exporter = new JRPdfExporter()
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream)

            //屏蔽copy功能
//            exporter.setParameter(JRPdfExporterParameter.,Boolean.TRUE)
            response.setHeader("Set-Cookie", "fileDownload=true; path=/")
            response.setContentType("application/pdf;charset=utf-8")
//            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".pdf")
            String disposition = FileBrowserUtil.getContentDisposition(downloadFileName + ".pdf", request)
            response.setHeader("Content-Disposition", disposition)
            response.setHeader("Connection", "close")
            response.setCharacterEncoding("utf-8")
            exporter.exportReport()
        } finally {
            try {
                outputStream.flush()
                outputStream.close()
            } catch (Exception e) {
            }
        }
    }

    /**
     * excel导出
     */
    private static void exportExcel(HttpServletRequest request, HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream outputStream = response.getOutputStream()

        try {
            JRXlsExporter exporter = new JRXlsExporter()
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream)
            response.setContentType("application/vnd.ms-excel;charset=utf-8")
            response.setHeader("Set-Cookie", "fileDownload=true; path=/")
//            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".xls")
            response.setHeader("Content-Disposition", FileBrowserUtil.getContentDisposition(downloadFileName + ".xls", request))
            response.setHeader("Connection", "close")
            // 删除记录最下面的空行
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE)
            // 删除多余的ColumnHeader
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE)
            //禁用白色背景
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE)
            exporter.exportReport()
        } finally {
            try {
                outputStream.flush()
                outputStream.close()
            } catch (Exception e) {
            }
        }
    }

    /**
     * 导出word
     */
    private static void exportWord(HttpServletRequest request, HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream outputStream = response.getOutputStream()

        try {
            JRExporter exporter = new JRRtfExporter()
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream)

            response.setContentType("application/msword;charset=utf-8")
            response.setHeader("Set-Cookie", "fileDownload=true; path=/")
            response.setHeader("Content-Disposition", FileBrowserUtil.getContentDisposition(downloadFileName + ".doc", request))
            response.setHeader("Connection", "close")
            exporter.exportReport()
        } finally {
            try {
                outputStream.flush()
                outputStream.close()
            } catch (Exception e) {
            }
        }
    }

    /**
     * 导出RTF
     */
    private static void exportRTF(HttpServletRequest request, HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream outputStream = response.getOutputStream()

        try {
            JRExporter exporter = new JRRtfExporter()
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream)

            response.setContentType("application/rtf;charset=utf-8")
            response.setHeader("Set-Cookie", "fileDownload=true; path=/")
//            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".rtf")
            response.setHeader("Content-Disposition", FileBrowserUtil.getContentDisposition(downloadFileName + ".pdf", request))
            response.setHeader("Connection", "close")
            exporter.exportReport()
        } finally {
            try {
                outputStream.flush()
                outputStream.close()
            } catch (Exception e) {
            }
        }
    }

    /**
     * 导出html
     */
    private static void exportHtml(HttpServletRequest request, HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream outputStream = response.getOutputStream()

        try {
            JRHtmlExporter exporter = new JRHtmlExporter()


            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream)
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8")
            //默认情况下用的是px,会导致字体缩小
            exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, "pt")
            //移除空行
            exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE)
            //线条不对齐的解决方法
            exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, Boolean.FALSE)
            exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE)
            response.setContentType("text/html;charset=utf-8")
            response.setHeader("Connection", "close")
            exporter.exportReport()
        } finally {
            try {
                outputStream.flush()
                outputStream.close()
            } catch (e) {
            }
        }
    }

    /**
     * 打印报表
     * @param response
     * @param jasperPrint
     * @param downloadFileName
     * @throws JRException* @throws IOException
     */
    private static void exporPrint(HttpServletRequest request, HttpServletResponse response, JasperPrint jasperPrint, String downloadFileName)
            throws JRException, IOException {
        ServletOutputStream outputStream = response.getOutputStream()

        try {
            JRHtmlExporter exporter = new JRHtmlExporter()


            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream)
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8")
            //默认情况下用的是px,会导致字体缩小
            exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, "pt")
            //移除空行
            exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE)
            //线条不对齐的解决方法
            exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, Boolean.FALSE)
            exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE)
            response.setContentType("text/html;charset=utf-8")
            response.setHeader("Connection", "close")

            //直接调用本地打印机打印
            JasperPrintManager.printReport(jasperPrint, false)
            exporter.exportReport()
        } finally {
            try {
                outputStream.flush()
                outputStream.close()
            } catch (e) {
            }
        }
    }

    /**
     * 打包zip
     */
    private static void compressFile(ByteArrayOutputStream stream, ZipOutputStream zos, String fileName) throws IOException {
        ZipEntry entry = new ZipEntry(fileName)
        zos.putNextEntry(entry)
        byte[] b = new byte[10 * 1024]
        int temp = 0
        ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray())
        while ((temp = bis.read(b)) != -1) {
            zos.write(b, 0, temp)
        }
//        zos.write(baos.toByteArray())
        zos.closeEntry()
        zos.flush()
    }
}

```
