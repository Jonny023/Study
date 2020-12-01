# 导出excel

```xml
  <dependency>
   <groupId>org.apache.poi</groupId>
   <artifactId>poi-ooxml</artifactId>
   <version>4.1.2</version>
  </dependency>
```

```java
package com.a.common.annations;

import java.lang.annotation.*;

/**
 * @description
 * @author Jonny
 * @date 2020/12/1 14:26
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelHeader {

    /**
     * 表头
     * @return
     */
    String value() default "";

    /**
     * 列索引
     * @return
     */
    int columnIndex() default 0;
}

```

* 实体类

```java
package com.a.business.entity;

import com.a.common.annations.ExcelHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jonny
 * @description
 * @date 2020/12/1 14:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDemo {

    @ExcelHeader(value = "手机号")
    private String phone;

    @ExcelHeader(value = "用户ID")
    private String userId;

    @ExcelHeader(value = "token")
    private String token;

}
```

* 测试类

```java
package com.a;

import com.a.business.entity.UserDemo;
import com.a.common.annations.ExcelHeader;
import com.a.common.entity.PageData;
import com.a.common.service.RedisService;
import com.a.common.util.RedisConst;
import com.a.plantform.mapper.dsno1.system.UserMapper;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 查询导出excel
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GDmainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("kunpeng")
public class QueryExcelTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 15178702000
     * 15178702199
     */
    @SneakyThrows
    @Test
    public void exec() {
        PageData param = null;
        PageData user;
        List<UserDemo> users = new ArrayList<>();
        UserDemo userDemo;
        for (int i = 0; i < 200; i++) {
            param = new PageData();
            param.put("PHONE", "15178702" + String.format("%03d", i));
            user = userMapper.findByPhoneOrJobNum(param);
            userDemo = new UserDemo();
            userDemo.setPhone(user.getString("PHONE"));
            userDemo.setUserId(user.getString("USER_ID"));
            userDemo.setToken(redisService.getString(RedisConst.PC + RedisConst.PREFIX_USER_TOKEN + param.getString("PHONE")));
            users.add(userDemo);
//            System.out.println(JSON.toJSONString(user));
//            System.out.println(redisService.get(RedisConst.PC + RedisConst.PREFIX_USER_TOKEN + param.getString("PHONE")));
        }

        // 文件路径
        String basePath = "D:/exportFile";
        String fileName = "用户信息_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() +".xls";

        // 将Excel写入文件
        HSSFWorkbook workbook = exportExcel(users, UserDemo.class);
        workbook.setSheetName(0,"sheetName");//设置sheet的Name

        // 无论是通过HttpServletResponse导出还是导出到本地磁盘,本质都是IO操作，所以这里将IO操作提取到外层。
        workbook.write(new File(basePath + File.separator + fileName));

    }

    /**
     *
     * @param data 需要导出的数据
     * @param clz 数据对应的实体类
     * @param <T> 泛型
     * @return Excel文件
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public <T> HSSFWorkbook exportExcel(List<T> data, Class<T> clz) throws NoSuchFieldException, IllegalAccessException {

        Field[] fields = clz.getDeclaredFields();
        List<String> headers = new LinkedList<>();
        List<String> variables = new LinkedList<>();

        // 创建工作薄对象
        HSSFWorkbook workbook=new HSSFWorkbook();//这里也可以设置sheet的Name
        // 创建工作表对象
        HSSFSheet sheet = workbook.createSheet();
        // 创建表头
        Row rowHeader = sheet.createRow(0);

        // 表头处理
        for (int h = 0; h < fields.length; h++) {
            Field field = fields[h];
            if (field.isAnnotationPresent(ExcelHeader.class)) {
                // 表头
                ExcelHeader annotation = field.getAnnotation(ExcelHeader.class);
                headers.add(annotation.value());
                rowHeader.createCell(h).setCellValue(annotation.value());

                // 字段
                variables.add(field.getName());
            }
        }

        // 数据处理
        for (int i = 0; i < data.size() ; i++) {

            //创建工作表的行(表头占用1行, 这里从第二行开始)
            HSSFRow row = sheet.createRow(i + 1);
            // 获取一行数据
            T t = data.get(i);
            Class<?> aClass = t.getClass();
            // 填充列数据
            for (int j = 0; j < variables.size(); j++) {

                Field declaredField = aClass.getDeclaredField(variables.get(j));
                declaredField.setAccessible(true);

                String key = declaredField.getName();
                Object value = declaredField.get(t);

                row.createCell(j).setCellValue(value.toString());
            }
        }
        return workbook;
    }

}
```

