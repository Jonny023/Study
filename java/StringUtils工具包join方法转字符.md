# 作用

* 将集合、数组元素通过指定字符连接为一个字符串

> StringUtils.join()跟JDK1.8里面的String.join()差不多

### 依赖

```xml
<dependency>
	<groupId>commons-lang</groupId>
	<artifactId>commons-lang</artifactId>
	<version>2.6</version>
</dependency>

```

### 具体代码

```java
public static void main(String[] args) {

	List list = new ArrayList();

	list.add("市场");
	list.add("销售");
	list.add("运维");

	// 集合
	String str = StringUtils.join(list,",");
	System.out.println(str);

	// 数组
	Object[] arrs = {10010,10011,10086,10000};
	String s = StringUtils.join(arrs,",");
	System.out.println(s);

}
```

> 执行结果

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2018/1101/70907dcdf5b84e589bfe9b6b8a33fb46.png)

## 注意
* StringUtils转数组的时候，只能是Object类型的数组
