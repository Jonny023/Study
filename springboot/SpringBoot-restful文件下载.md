# RestController下载文件

* controller

```java

@RestController
@RequestMapping("/file")
public class FileController {

 	@Resource
 	private FileService fileService;

	@ApiOperation("下载")
    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, consumes = "*/*")
    public void download(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        fileService.download(id, request, response);
    }
}
```

* service impl

```java
@Override
public void download(Long id, HttpServletRequest request, HttpServletResponse response) {
    File file = baseMapper.selectById(id);
    if (file != null) {
        String outputPath = String.format("%s/%s", config.getPath(), file.getAnnexUrl());
        File outFile = null;
        try {
            //压缩
//                response.setContentType("application/octet-stream");
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", FileDownloadUtil.getContentDisposition(file.getAnnexName().substring(0,file.getAnnexName().indexOf(".")), "csv", request));
            OutputStream out = response.getOutputStream();
            outFile = new File(outputPath);
            InputStream in = new FileInputStream(outFile);

            // 循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            LOG.error("{}", e);
        }
    }
}
```