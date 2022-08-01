## springboot-日期转换器

> 实体类入参定义为：前端传字符串的日期自动转换为LocalDate、LocalDateTime

```java
package com.example.springbootdemo.config;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Jonny
 */
@Configuration
public class DateConverterConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    /**
     * 默认年月格式
     */
    public static final String DEFAULT_YEAR_MONTH_FORMAT = "yyyy-MM";

    /**
     * 序列化器
     *
     * @param jacksonObjectMapperBuilder
     */
    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // LocalDateTime序列化器
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));

        // LocalDateTime序列化器
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));

        // LocalTime序列化器
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        // YearMonth序列化器
        javaTimeModule.addSerializer(YearMonth.class,
                new YearMonthSerializer(DateTimeFormatter.ofPattern(DEFAULT_YEAR_MONTH_FORMAT)));

        // Date序列化器
        javaTimeModule.addSerializer(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(DateUtil.format(date, DATE_TIME_FORMAT));
            }
        });


        /*
         * LocalDateTime反序列化器
         */
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                if (StringUtils.isEmpty(text)) {
                    return null;
                }
                if (StringUtils.isNumeric(text)) {
                    return Instant.ofEpochMilli(Long.parseLong(text)).atZone(ZoneId.systemDefault()).toLocalDateTime();
                } else {
                    return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
                }
            }
        });

        /*
         *  LocalDate反序列化器
         */
        javaTimeModule.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                if (StringUtils.isBlank(text)) {
                    return null;
                }
                if (StringUtils.isNumeric(text)) {
                    return Instant.ofEpochMilli(Long.parseLong(text)).atZone(ZoneId.systemDefault()).toLocalDate();
                } else {
                    return LocalDate.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
                }
            }
        });

        // LocalTime反序列化器
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        // YearMonth反序列化器
        javaTimeModule.addDeserializer(YearMonth.class,
                new YearMonthDeserializer(DateTimeFormatter.ofPattern(DEFAULT_YEAR_MONTH_FORMAT)));

        // Date反序列化器
        javaTimeModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                if (StringUtils.isBlank(text)) {
                    return null;
                }
                if (StringUtils.isNumeric(text)) {
                    return new Date(Long.parseLong(text));
                } else {
                    return DateUtil.parse(text);
                }
            }
        });

        // 添加进去
        jacksonObjectMapperBuilder.modules(javaTimeModule);
    }

}

```
