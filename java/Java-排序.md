## 冒泡排序

* 循环比较前后两个值的大小，比较`n * n`次，通过中间变量交换值

```java
static int[] array(int[] ars) {
    int temp;
    int size = ars.length;
    for (int i = 1; i < size; i++) {
        for (int j = 0; j < size - 1; j++) {
            if(ars[j] > ars[j+1]) {
                temp = ars[j];
                ars[j] = ars[j+1];
                ars[j+1] = temp;
            }
        }
    }
    return ars;
}
```
