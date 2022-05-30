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

### 归并排序

```java
/**
 *  归并排序：mergeSort(arr, 0, arr.length - 1)
 * @param array
 * @param low
 * @param high
 */
public static void mergeSort(int[] array, int low, int high) {
    if (low >= high) {
        return;
    }
    int mid = (high + low) >>> 1;
    mergeSort(array, low, mid);
    mergeSort(array, mid + 1, high);
    merge(array, low, mid, high);
}
private static void merge(int[] array, int low, int mid, int high) {
    int[] tempArray = new int[high - low + 1];
    int k = 0;
    int s1 = low;
    int s2 = mid + 1;
    while (s1 <= mid && s2 <= high) {
        if (array[s1] < array[s2]) {
            tempArray[k++] = array[s1++];
        } else {
            tempArray[k++] = array[s2++];
        }
    }
    while (s1 <= mid) {
        tempArray[k++] = array[s1++];
    }
    while (s2 <= high) {
        tempArray[k++] = array[s2++];
    }
    for (int i = 0, len = tempArray.length; i < len; i++) {
        array[i + low] = tempArray[i];
    }
}
```
