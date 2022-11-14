# 常用笔记

```java
// @ApiParam(value = "file")声明文件，可以用@RequestParam或者@RequestPart标识文件
public Response importProject(@ApiParam(value = "file", required = true) @NotNull(message = "file不能为空") @RequestParam MultipartFile file) {
    
}
```

### 参数列表传date参数

* ApiImplicitParams
   * dataType
     * int
     * string
     * long
     * date
     * map
     * file 对应paramType的form
     * 实体对象 对应paramType的body
   * paramType
     * header
     * query
     * path
     * body 对应dataType的实体对象 map
     * form 

```java
@GetMapping("/list")
@ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "姓名", dataType = "string", required = false),
        @ApiImplicitParam(name = "queryDate", value = "查询日期", dataType = "date", example = "2022-11-14", required = false)
})
public R<User> getUserList(String name, @DateTimeFormat(pattern = DatePatternConst.YYYY_MM_DD) Date queryDate) {
}
```
