# JPA null值不更新

> 网上很多说`@DynamicUpdate`可以实现`null`值不更新，那都是谬论
>
> 作用：这个注解的作用是更新前比对提交的数据和数据库保存的数据是否一致，若一致则不做更新，否则会更新数据库的值，为`null`就会覆盖原先的数据

* 新增工具类，作用：只拷贝不为`null`的属性

```java
package cn.com.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.stream.Stream;

/**
 * JPA避免一些空值对于动态部分更新的影响工具类
 * (null值不更新, 配合实体类@DynamicUpdate使用)
 */
public class JpaUtil {

    /**
     * 查询出entity值 -> JpaUtil.copyNotNullProperties(input, entity); -> save(entity)
     *
     * @param input  输入实体类
     * @param entity 数据库查询出的实体类
     */
    public static void copyNotNullProperties(Object input, Object entity) {
        BeanUtils.copyProperties(input, entity, getNullPropertyNames(input));
    }

    /**
     * 忽略的字段：值为null、浮点数为0.0、值为空字符串""
     * <p>
     * ignoreProperties {@link BeanUtils#copyProperties(java.lang.Object, java.lang.Object, java.lang.String...)}
     *
     * @param object 传入全部字段
     * @return 忽略的字段
     */
    private static String[] getNullPropertyNames(Object object) {
        final BeanWrapperImpl wrapper = new BeanWrapperImpl(object);
        return Stream.of(wrapper.getPropertyDescriptors()).map(PropertyDescriptor::getName)
                .filter(propertyName -> wrapper.getPropertyValue(propertyName) == null
                        || "0.0".equals(String.valueOf(wrapper.getPropertyValue(propertyName)))
                        || "".equals(String.valueOf(wrapper.getPropertyValue(propertyName))))
                .toArray(String[]::new);
    }

}
```

* 实体类

```java
package cn.com.dao.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@DynamicUpdate
@ApiModel("角色表")
@Table(name = "sys_role")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_role", comment = "角色表")
public class SysRole implements Serializable {

	private static final long serialVersionUID =  9022888214467860659L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
   	@Column(name = "id")
	@ApiModelProperty(value = "主键")
	private Long id;

	@Column(name = "bid")
	@ApiModelProperty(value = "业务id")
	private String bid;

   	@Column(name = "parent_bid")
	@ApiModelProperty(value = "父业务id")
	private String parentBid;

   	@Column(name = "name")
	@ApiModelProperty(value = "角色名称")
	private String name;

   	@Column(name = "enname")
	@ApiModelProperty(value = "角色英文名称")
	private String enname;

   	@Column(name = "description")
	@ApiModelProperty(value = "备注")
	private String description;

	@CreatedDate
   	@Column(name = "create_time", updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@LastModifiedDate
   	@Column(name = "update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "更新时间")
	private Date updateTime;

	@CreatedBy
   	@Column(name = "create_user", updatable = false)
	@ApiModelProperty(value = "创建人")
	private String createUser;

	@LastModifiedBy
   	@Column(name = "update_user")
	@ApiModelProperty(value = "更新人")
	private String updateUser;

}
```

* 更新

```java
// 先查询出来
SysRole exists = this.findByEnNameAndLogicDelete(form.getEnname(), Boolean.FALSE);
if (exists != null) {
    // 提交的不为null的值覆盖数据库的值
    JpaUtil.copyNotNullProperties(role, exists);
    baseRepository.save(exists);
}
```

