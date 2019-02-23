# Gson基本用法

> gradle

```
compile group: 'com.google.code.gson', name: 'gson', version: '2.8.0'
```

> maven 

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.0</version>
</dependency>
```

## 用法

```java
package com;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import javax.persistence.PersistenceUnit;
import java.util.List;

/**
 * @Author Lee
 * @Description
 * @Date 2019年02月23日 20:08
 */
public class Hi {

    class Car {

        Integer id;
        String name;
        String sex;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }
    }


    @Test
    public void main() {

        GsonBuilder builder = new GsonBuilder();

        // 禁止转义html标签
        builder.disableHtmlEscaping();

        // 启用转换后首字母大写，id转为Id（命名策略）
        //builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);

        // 格式化json串
        builder.setPrettyPrinting();

        // 序列化为null
        builder.serializeNulls();
        builder.setLenient();

        Gson gson = builder.create();

        // 单个对象
        String json = "{\"id\":1,\"name\":\"张三 \",\"sex\":\"男\"}";
        Car car = gson.fromJson(json,Car.class);
        System.out.println(car);

        Car car1 = new Car();
        car1.setId(2);
        car1.setName("<a>sd</a>");
        car1.setSex("女");
        System.out.println(gson.toJson(car));
        System.out.println(gson.toJson(car1));

        // 集合对象
        String jsons = "[{\"id\":1,\"name\":\"张三\",\"sex\":\"男\"},{\"id\":2,\"name\":\"张1\",\"sex\":\"男\"}]";
        List<Car> cars = gson.fromJson(jsons,new TypeToken<List<Car>>() {}.getType());
        for (Car c : cars) {
            System.out.println(c);
        }

        System.out.println(gson.toJson(cars));
    }
}
```
