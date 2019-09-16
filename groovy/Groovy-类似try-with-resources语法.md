# `try-with-resources`不能再`groovy`类中使用，通过`withCloseable`替代

```groovy
this.class.getResourceAsStream(template).withCloseable { inputStream ->
    //output 为导出的Excel路径，有一个坑需要注意的是，在springboot项目打包成jar包之后，
    //导出文件路径可这样表示： System.getProperty("user.dir") + "/export_leadshow.xls"，这种"static/export_leadshow.xls"路径会失效
    new FileOutputStream(path + File.separator + fileName + ".xls").withCloseable { os ->
        Context context = new PoiContext()
        context.putVar("datas", datas);
        JxlsHelper.getInstance().processTemplate(inputStream, os, context);
    }
}
```
