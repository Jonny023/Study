package cn.com.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @description:
 * @author: Jonny
 * @date: 2021-04-16
 */
@Data
@Accessors(chain = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     *  主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    /**
     *  业务ID
     */
    @Column(name = "bid")
    private String bid;

    /**
     *  创建时间
     */
    @CreatedDate
    @Column(name = "create_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     *  更新时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     *  创建人
     */
    @CreatedBy
    @Column(name = "create_user", updatable = false)
    private String createUser;

    /**
     *  更新人
     */
    @LastModifiedBy
    @Column(name = "update_user")
    private String updateUser;

}
