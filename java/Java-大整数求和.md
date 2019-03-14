## 大整数求和,不能用`BigInteger`

> 原理

* 小学草稿列出式子的方式 

```java
public class BigIntMulti {

    public static void main(String[] args) {
        System.out.println(multiply("222222222222222", "111111111111111"));
    }

    public static String multiply(String num1, String num2) {
        int l = num1.length();
        int r = num2.length();
        //用来存储结果的数组，可以肯定的是两数相乘的结果的长度，肯定不会大于两个数各自长度的和。
        int[] num = new int[l + r];
        //第一个数按位循环
        for (int i = 0; i < l; i++) {
            //得到最低位的数字
            int n1 = num1.charAt(l - 1 - i) - '0';
            //保存进位
            int tmp = 0;
            //第二个数按位循环
            for (int j = 0; j < r; j++) {
                int n2 = num2.charAt(r - 1 - j) - '0';
                //拿出此时的结果数组里存的数+现在计算的结果数+上一个进位数
                tmp = tmp + num[i + j] + n1 * n2;
                //得到此时结果位的值
                num[i + j] = tmp % 10;
                //此时的进位
                tmp /= 10;
            }
            //第一轮结束后，如果有进位，将其放入到更高位
            num[i + r] = tmp;
        }

        int i = l + r - 1;
        //计算最终结果值到底是几位数，
        while (i > 0 && num[i] == 0) {
            i--;
        }
        StringBuilder result = new StringBuilder();
        //将数组结果反过来放，符合正常读的顺序，
        //数组保存的是：1 2 3 4 5
        //但其表达的是54321，五万四千三百二十一。
        while (i >= 0) {
           result.append(num[i--]);
        }
        return result.toString();
    }
}

```
