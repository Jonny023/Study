#### 代码

```java
package com.test1;

import com.spire.pdf.FileFormat;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author Jonny
 * @description
 * @date 2020/8/4 14:34
 */
public class XlsTests {

    // https://zhuanlan.zhihu.com/p/112199694
    public static void main(String[] args) throws Exception {

        //加载Excel文档
//        Workbook wb = new Workbook();
//        wb.loadFromFile("e:\\file\\excel.xls");
//
//        //调用方法保存为PDF格式
//        wb.saveToFile("d:\\file\\convert.pdf", FileFormat.PDF);

        //加载Excel文档
        Workbook wb = new Workbook();
        wb.loadFromFile("e:\\file\\excel.xls");


        //获取第2个工作表
//        Worksheet sheet = wb.getWorksheets().get(0);

        //调用方法保存为PDF格式
//        sheet.saveToPdf("convert.pdf");
//        wb.saveToFile("E:\\file\\pdf.pdf", FileFormat.PDF);

        Worksheet sheet = wb.getWorksheets().get(0);
        sheet.saveToImage("e:\\file\\out.png");
        File file = new File("e:\\file\\out.png");
        int width = getImgWidth(file);
        int height = getImgHeight(file);
        cutImage("e:\\file\\out.png", 1, 50, width, height);
    }

    /**
     * 图片剪切
     *
     * @param filePath
     * @param x
     * @param y
     * @param w
     * @param h
     * @throws Exception
     */
    public static void cutImage(String filePath, int x, int y, int w, int h) throws Exception {
        ImageInputStream iis = ImageIO.createImageInputStream(new FileInputStream(filePath));
        @SuppressWarnings("rawtypes")
        Iterator it = ImageIO.getImageReaders(iis);
        ImageReader imagereader = (ImageReader) it.next();
        imagereader.setInput(iis);
        ImageReadParam par = imagereader.getDefaultReadParam();
        par.setSourceRegion(new Rectangle(x, y, w, h));
        BufferedImage bi = imagereader.read(0, par);
        ImageIO.write(bi, "png", new File(filePath));
    }

    /**
     * 获取图片宽度
     *
     * @param file 图片文件
     * @return 宽度
     */
    public static int getImgWidth(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int ret = -1;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getWidth(null); // 得到源图宽
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 获取图片高度
     *
     * @param file 图片文件
     * @return 高度
     */
    public static int getImgHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int ret = -1;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getHeight(null); // 得到源图高
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}

```
