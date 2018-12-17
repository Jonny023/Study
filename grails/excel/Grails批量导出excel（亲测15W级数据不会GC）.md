## 依赖

```gradle
compile 'org.apache.poi:poi-scratchpad:3.10-FINAL'
compile "org.apache.poi:poi:3.14"
compile "org.apache.poi:poi-scratchpad:3.14"
compile "org.apache.poi:poi-ooxml:3.14"
compile "org.apache.poi:poi-ooxml-schemas:3.14"
compile "org.apache.poi:ooxml-schemas:1.3"
```

## 服务类

```groovy
package com.common

import grails.transaction.Transactional
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFFont
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.streaming.SXSSFCell
import org.apache.poi.xssf.streaming.SXSSFRow
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFRichTextString

import javax.servlet.http.HttpServletResponse

@Transactional(readOnly = true)
class ExcelService {

    /**
     * Excel导出
     * @param title String 标题
     * @param headers ArrayList<String> 表头
     * @param fileName String 导出文件名
     * @param datas ArrayList<Map> map类型集合数据
     * @param out 输出流
     */
    def export(title,headers,fileName,datas,HttpServletResponse response) throws Exception {
        if(!datas) {
            return
        }

        //声明行
        int rownum=0
        int rowaccess=100 //100条

        OutputStream out = response.outputStream

        // 声明一个工作薄
//        HSSFWorkbook workbook = new HSSFWorkbook()
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000)
        // 生成一个表格
        Sheet sheet = (SXSSFSheet)workbook.createSheet(title)
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15)
        // 生成一个样式
        CellStyle style = workbook.createCellStyle()
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index)
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND)
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN)
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN)
        style.setBorderRight(HSSFCellStyle.BORDER_THIN)
        style.setBorderTop(HSSFCellStyle.BORDER_THIN)
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER)
        // 生成一个字体
        Font font = workbook.createFont()
        font.setColor(HSSFColor.BLACK.index)
        font.setFontHeightInPoints((short) 12)
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD)
        // 把字体应用到当前的样式
        style.setFont(font)
        // 生成并设置另一个样式
        CellStyle style2 = workbook.createCellStyle()
        style2.setFillForegroundColor(HSSFColor.WHITE.index)
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND)
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN)
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN)
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN)
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN)
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER)
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER)
        // 生成另一个字体
        Font font2 = workbook.createFont()
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL)
        // 把字体应用到当前的样式
        style2.setFont(font2)

        // 产生表格标题行
        SXSSFRow row = sheet.createRow(0)
        for (int i = 0; i < headers.size(); i++) {
            SXSSFCell cell = row.createCell(i)
            cell.setCellStyle(style)
//            RichTextString text = new RichTextString(headers[i])
            XSSFRichTextString text = new XSSFRichTextString(headers[i])
            text.applyFont(font)
            cell.setCellValue(text)
        }

        //遍历集合数据，产生数据行
        datas.eachWithIndex {d,index ->
            //循环省略每循环一次rownum++
            if(rownum%rowaccess==0){
                try {
                    ((SXSSFSheet) sheet).flushRows()
                } catch (IOException e) {
                    e.printStackTrace()
                }
            }
            row = sheet.createRow(index+1)
            Set set = d.entrySet()
            for(int i=0;i<set.size();i++) {
                SXSSFCell cell = row.createCell(i)
                //单元格样式
                cell.setCellStyle(style2)
                Object value = set[i].value
                String textValue = value?.toString()

                if (textValue) {
                    cell.setCellValue(textValue)
                }
            }
            rownum++
        }

        try {
            response.setCharacterEncoding("utf-8")
            response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"))
            response.setContentType("application/vnd.ms-excel;charset=utf-8")
            workbook.write(out)
            out.flush()
            out.close()
        } catch (IOException e) {
            e.printStackTrace()
        } finally {
            if(out) {
                out.close()
            }
        }
    }
}
```

## 中文标点符号乱码？

```groovy
response.setCharacterEncoding("utf-8")
String userAgent = request.getHeader("User-Agent")
byte[] bytes = userAgent.contains("MSIE") ? fileName.getBytes(): fileName.getBytes("UTF-8") // fileName.getBytes("UTF-8")处理safari的乱码问题
fileName = new String(bytes, "ISO-8859-1") // 各浏览器基本都支持ISO编码
response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", fileName))
response.setContentType("application/vnd.ms-excel;charset=utf-8")
```
