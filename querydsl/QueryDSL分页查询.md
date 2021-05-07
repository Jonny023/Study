## QueryDSL分页查询

* 转换工具

```java
package cn.com.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * @description 类型转换器
 */
public class Convertor {

    public static <T> T convert(Object vo, T t) {
        if (vo == null) {
            return null;
        }
        copyProperties(vo, t);
        return t;
    }

    public static <T> T convert(Object vo, Supplier<T> target) {
        if (vo == null) {
            return null;
        }
        T t = target.get();
        copyProperties(vo, t);
        return t;
    }

    public static <T> T convert(Object vo, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        if (vo == null) {
            return null;
        }
        T object = clazz.newInstance();
        copyProperties(vo, object);
        return object;
    }

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 使用场景：Entity、Bo、Vo层数据的复制，因为BeanUtils.copyProperties只能给目标对象的属性赋值，却不能在List集合下循环赋值，因此添加该方法
     * 如：List<AdminEntity> 赋值到 List<AdminVo> ，List<AdminVo>中的 AdminVo 属性都会被赋予到值
     * S: 数据源类 ，T: 目标类::new(eg: AdminVo::new)
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());

        T t = null;
        for (S source : sources) {
            t = target.get();
            copyProperties(source, t);
            if (callBack != null) {
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }

}
```



* 分页类

```java
package cn.com.api.base;

import cn.com.util.Convertor;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.querydsl.QPageRequest;

import java.util.List;
import java.util.function.Supplier;

/**
 * @description: 分页类
 * @author: Jonny
 * @date: 2021-04-02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageVO<T> {

    /**
     *  最大限制条数
     */
    public static final int MAX_SIZE = 20;

    /**
     *  初始限制条数
     */
    public static final int INIT_SIZE = 10;

    /**
     *  第一页
     */
    public static final int FIRST_PAGE = 1;

    @ApiModelProperty(value = "当前页面")
    private int currentPage;

    @ApiModelProperty(value = "总页数")
    private Long totalPage;

    @ApiModelProperty(value = "总记录数")
    private Long totalCount;

    @ApiModelProperty(value = "每页显示记录数")
    private int pageSize;

    private List<T> data;

    /**
     *  封装分页
     * @param JPAQuery query
     * @param request
     * @return
     */
    public static <T> PageVO<T> of(JPAQuery<T> query, QPageRequest request) {
        request = init(request);
        QueryResults<T> results = query.offset((request.getPageNumber()) * request.getPageSize()).limit(request.getPageSize()).fetchResults();
        PageVO<T> pageVO = (PageVO<T>) PageVO.builder()
                .currentPage(request.getPageNumber() + 1)
                .pageSize(request.getPageSize())
                .totalPage((results.getTotal() + request.getPageSize() - 1) / request.getPageSize())
                .totalCount(results.getTotal())
                .data((List<Object>) results.getResults())
                .build();
        return pageVO;
    }

    /**
     *  封装分页
     * @param JPAQuery query
     * @param request
     * @return
     */
    public static <T, R> PageVO<R> of(JPAQuery<T> query, QPageRequest request, Supplier<R> target) {
        request = init(request);
        JPAQuery<T> jpaQuery = query.offset((request.getPageNumber()) * request.getPageSize()).limit(request.getPageSize());
        long count = jpaQuery.fetchCount();
        PageVO<T> pageVO = (PageVO<T>) PageVO.builder()
                .currentPage(request.getPageNumber() + 1)
                .pageSize(request.getPageSize())
                .totalPage((count + request.getPageSize() - 1) / request.getPageSize())
                .totalCount(count)
                .data((List<Object>) jpaQuery.fetch())
                .build();
        if (target != null) {
            PageVO resultVO = new PageVO();
            resultVO.setCurrentPage(pageVO.getCurrentPage());
            resultVO.setPageSize(pageVO.getPageSize());
            resultVO.setTotalCount(pageVO.getTotalCount());
            resultVO.setTotalPage(pageVO.getTotalPage());
            resultVO.setData(Convertor.copyListProperties(pageVO.getData(), target));
            return resultVO;
        }
        return (PageVO<R>) pageVO;
    }

    private static QPageRequest init(QPageRequest request) {
        if (request == null) {
            return QPageRequest.of(FIRST_PAGE, INIT_SIZE);
        }
        if (request.getPageNumber() <= 0) {
            request = (QPageRequest) request.next();
        }
        if (request.getPageSize() <= 0) {
            request = QPageRequest.of((int) request.getOffset(), INIT_SIZE);
        }
        // 超过最大限制条数设置为默认限制大小
        if(request.getPageSize() >= MAX_SIZE) {
            request = QPageRequest.of((int)request.getPageNumber(), MAX_SIZE);
        }
        request = (QPageRequest) request.previous();
        return request;
    }
}
```

* 业务类

```java
    @Override
    public PageVO list(RoleQueryForm form) {
        QSysRole qRole = QSysRole.sysRole;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRole.logicDelete.eq(Boolean.FALSE));
        if (StringUtils.hasText(form.getRoleName())) {
            builder.and(qRole.name.like("%".concat(form.getRoleName().concat("%"))));
        }
        JPAQuery<SysRole> query = super.jpaQueryFactory
                .select(qRole)
                .from(qRole)
                .where(builder);
        QPageRequest page = QPageRequest.of(form.getCurrentPage(), form.getPageSize());
        return PageVO.of(query, page, RoleVO::new);
    }
```

