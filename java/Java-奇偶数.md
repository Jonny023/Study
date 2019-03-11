## Java判断奇偶

```java
// 方式一
private int deal(int data) {
    return data&1;
}

// 方式二
private boolean isEvenNumber(int n) {
    return (((n >> 1) << 1) == n);
}
```
