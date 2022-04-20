# mat（Eclipse Memory Analyzer）内存分析工具

[参考](https://blog.csdn.net/chengqiuming/article/details/119965267)

## 下载地址

[下载](https://www.eclipse.org/mat/)

[需jdk11+](http://jdk.java.net/17/)

> MemoryAnalyzer需要jdk11+

* 修改MemoryAnalyzer.ini文件在-vmargs前面添加配置后再次启动

```bash
-vm
C:\Program Files\Java\jdk-17.0.2\bin\javaw.exe
```

## 打开hprof

> hprof文件可以通过dump导出

* 主要分析Overview下
	* `Histogram` 
	* `LeakSupects`
