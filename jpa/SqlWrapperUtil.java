package com.example.springbootclickhouse.utils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Table;
import org.springframework.util.CollectionUtils;

/**
 * @author: Jonny
 * @description: sql批处理构建工具
 * @date:created in 2021/4/29 15:05
 * @modificed by:
 */
public class SqlWrapperUtil {

  /**
   * 构建insert sql
   */
  public static <T> String[] buildInsert(List<T> lists) {
    if (CollectionUtils.isEmpty(lists)) {
      throw new RuntimeException("The lists is not be empty.");
    }
    StringBuilder sb = null;
    List<List<Map<String, Object>>> list = collect(lists);
    int len = list.size();
    String[] sqlArray = new String[len];
    List<Map<String, Object>> dataList;
    for (int i = 0; i < len; i++) {
      sb = new StringBuilder();
      sb.append("insert into ");
      sb.append(table(lists.get(0).getClass()));
      dataList = list.get(i);
//      if (i == 0) {
        sb.append("(");
        sb.append(dataList.stream().filter(map -> map.containsKey("columnName"))
            .map(map -> map.get("columnName").toString())
            .collect(Collectors.joining(",")));
        sb.append(") values");
        sb.append(dataList.stream().map(map -> String.valueOf(map.get("value")))
            .map(v -> MessageFormat.format("{0}{1}{2}", "'", v, "'"))
            .collect(Collectors.joining(",", "(", ")")));
//      } else {
//        sb.append(",");
//        sb.append(dataList.stream().map(map -> String.valueOf(map.get("value")))
//            .map(v -> MessageFormat.format("{0}{1}{2}", "'", v, "'"))
//            .collect(Collectors.joining(",", "(", ")")));
//      }
      sqlArray[i] = sb.toString();
    }
    return sqlArray;
  }

  /**
   *
   *  获取表名
   */
  private static <T> String table(Class<T> clazz) {
    Table annotation = clazz.getAnnotation(Table.class);
    if (annotation == null) {
      throw new RuntimeException("Please add the table annotation.");
    }
    return annotation.name();
  }

  /**
   *
   *  获取数据值集合
   */
  private static <T> List<List<Map<String, Object>>> collect(List<T> lists) {
    List<List<Map<String, Object>>> result = lists.stream().map(data -> {
      try {
        return reflect(data);
      } catch (IllegalAccessException e) {
        return null;
      }
    }).collect(Collectors.toList());
    return result;
  }

  /**
   * 通过反射读取实例对象的字段及值
   */
  private static <T> List<Map<String, Object>> reflect(T bean) throws IllegalAccessException {
    if (bean == null) {
      return new ArrayList<>();
    }
    Field f;
    boolean flag;
    List<Map<String, Object>> list = new ArrayList<>();
    Map<String, Object> map;
    Field[] fields = bean.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      f = fields[i];
      f.setAccessible(true);
      map = new LinkedHashMap<>();
      flag = f.isAnnotationPresent(Column.class);
      if (flag) {
        map.put("columnName", f.getAnnotation(Column.class).name());
      }
      map.put("value", f.get(bean));
      list.add(map);
    }
    return list;
  }
}
