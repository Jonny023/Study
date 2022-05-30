# 排序

### 1.冒泡排序

```java
    /**
     *  冒泡排序： 升序
     * @param arr
     */
    public static void ascSort(int[] arr) {
        int len = arr.length;
        int temp;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    /**
     *  冒泡排序： 降序
     * @param arr
     */
    public static void descSort(int[] arr) {
        int len = arr.length;
        int temp;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
```
