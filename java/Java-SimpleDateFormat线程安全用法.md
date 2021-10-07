## SimpleDateFormat线程安全

```java
public static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
    @Override
    protected DateFormat initialValue() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

};

public static void main(String[] args) {
    System.out.println(DfUtil.df.get().format(new Date()));
}
```

