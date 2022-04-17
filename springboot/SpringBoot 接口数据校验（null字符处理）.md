# SpringBoot 接口数据校验(“null”字符转null)

## 问题

> 我有个接口是`表单传参`，`swagger`测试修改数据的时候`null`型id不传值，后端接收到的参数为：`{id:"null"}`，导致报错

```java
org.springframework.validation.BindException: org.springframework.validation.BeanPropertyBindingResult: 1 errors
Field error in object 'User' on field 'id': rejected value [null]; codes [typeMismatch.user.id,typeMismatch.id,typeMismatch.java.lang.Long,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [user.id,id]; arguments []; default message [id]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Long' for property 'id'; nested exception is java.lang.NumberFormatException: For input string: "null"]
```

## 解决方案

### 1.自定义转换

> 重写`PropertyEditorSupport`类的`setAsText`方法，把`null`字符处理下

```java
import org.springframework.lang.Nullable;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

public class LongPropertyEditor extends PropertyEditorSupport {

    private final Class<? extends Number> numberClass;
    @Nullable
    private final NumberFormat numberFormat;
    private final boolean allowEmpty;

    public LongPropertyEditor(Class<? extends Number> numberClass, boolean allowEmpty) throws IllegalArgumentException {
        this(numberClass, (NumberFormat) null, allowEmpty);
    }

    public LongPropertyEditor(Class<? extends Number> numberClass, @Nullable NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
        if (!Number.class.isAssignableFrom(numberClass)) {
            throw new IllegalArgumentException("Property class must be a subclass of Number");
        } else {
            this.numberClass = numberClass;
            this.numberFormat = numberFormat;
            this.allowEmpty = allowEmpty;
        }
    }

    /**
     *  此方法判断null字符
     * @param text
     * @throws IllegalArgumentException
     */
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            this.setValue((Object) null);
        } else if (this.numberFormat != null) {
            this.setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        } else if ("null".equals(text)) {
            this.setValue((Object) null);
        } else {
            this.setValue(NumberUtils.parseNumber(text, this.numberClass));
        }

    }

    public void setValue(@Nullable Object value) {
        if (value instanceof Number) {
            super.setValue(NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass));
        } else {
            super.setValue(value);
        }

    }

    public String getAsText() {
        Object value = this.getValue();
        if (value == null) {
            return "";
        } else {
            return this.numberFormat != null ? this.numberFormat.format(value) : value.toString();
        }
    }
}
```

### 2.自定义类

```java
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public abstract class BaseController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Long.class, new LongPropertyEditor(Long.class, true));
    }
}
```

### 3.控制器继承

```java
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {
    
}
```

