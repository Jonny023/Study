package com.jonny

import com.jonny.utils.JxlsV2Util

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
//                Context context = new PoiContext()
//                context.putVar("datas", datas)
//                JxlsHelper.getInstance().processTemplate(inputStream, os, context)
                JxlsV2Util.exportExcel(inputStream, os, [datas: datas])
            }
        }

        return [filePath: filePath, file: file]
    }
}