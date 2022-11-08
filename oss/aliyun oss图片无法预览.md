> 图片文件上传oss后，浏览器地址栏输入返回的链接无法查看图片，浏览器直接下载图片了，需要设置Content-Type为：image/jpg，不能设置为image/jpeg

```java
/**
 * 上传图片至OSS
 *
 * @param file 文件对象
 * @return String 返回不带http协议的url地址
 */
public String uploadImage2Oss(MultipartFile file, String folder) {
	String resultStr = null;
	//以输入流的形式上传文件
	try (InputStream is = file.getInputStream()) {
		//文件名
		String fileName = file.getOriginalFilename();
		//文件大小
		long fileSize = file.getSize();
		//创建上传Object的Metadata
		ObjectMetadata metadata = new ObjectMetadata();
		//上传的文件的长度
		metadata.setContentLength(is.available());
		//指定该Object被下载时的网页的缓存行为
		metadata.setCacheControl("no-cache");
		//指定该Object下设置Header
		metadata.setHeader("Pragma", "no-cache");
		metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
		//指定该Object被下载时的内容编码格式
		metadata.setContentEncoding("utf-8");
		//文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
		//如果没有扩展名则填默认值application/octet-stream
		metadata.setContentType(getContentType(fileName));
		//指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
		//metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
		metadata.setObjectAcl(CannedAccessControlList.PublicRead);
		//上传文件   (上传文件流的形式)
		PutObjectResult putResult = oss.putObject(BACKET_NAME, folder + fileName, is, metadata);
		//解析结果
		//resultStr = putResult.getETag();
		//获取文件访问地址
		resultStr = BACKET_NAME + "." + ENDPOINT + "/" + folder + fileName;
	} catch (Exception e) {
		log.error("上传阿里云OSS服务器异常: ", e);
	}
	return resultStr;
}



/**
 * 通过文件名判断并获取OSS服务文件上传时文件的contentType
 *
 * @param fileName 文件名
 * @return 文件的contentType
 */
public static String getContentType(String fileName) {
	//文件的后缀名
	String fileExtension = fileName.substring(fileName.lastIndexOf("."));
	if (".bmp".equalsIgnoreCase(fileExtension)) {
		return "image/bmp";
	}
	if (".gif".equalsIgnoreCase(fileExtension)) {
		return "image/gif";
	}
	if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)) {
		return "image/jpg";
	}
	if (".html".equalsIgnoreCase(fileExtension)) {
		return "text/html";
	}
	if (".txt".equalsIgnoreCase(fileExtension)) {
		return "text/plain";
	}
	if (".vsd".equalsIgnoreCase(fileExtension)) {
		return "application/vnd.visio";
	}
	if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
		return "application/vnd.ms-powerpoint";
	}
	if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
		return "application/msword";
	}
	if (".xml".equalsIgnoreCase(fileExtension)) {
		return "text/xml";
	}
	//默认返回类型
	return "image/jpg";
}


//方法调用
String folder = String.format("%s%s/", typeEnum.getPath(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
String path = ossUtil.uploadImage2Oss(file, folder);
return String.format("https://%s", path);

```
