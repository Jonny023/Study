# Java位运算

[参考地址](https://www.zhihu.com/question/38206659)

```java
//参考地址：https://www.zhihu.com/question/38206659
public static void main(String[] args) {
    //左移(左移1位,即乘2)
    //x = 100 * (2的3次方)
    int x = 100 << 3;
    System.out.println(x);//输出800

    //右移(右移1为,即除2)：y=16/(1*2) 除以2
    int y = 16 >> 2;
    System.out.println(y);//输出4

    //交换值
    int a = 1, b = 2;
    a ^= b;
    b ^= a;
    a ^= b;
    System.out.printf("a:%d, b:%d\n", a, b);
    a = a ^ b;
    b = a ^ b;
    a = a ^ b;
    System.out.printf("a:%d, b:%d\n", a, b);

    //判断奇数偶数(最后一位为0则为偶数，为1则为奇数)
    System.out.print("偶数：");
    for (int i = 0; i < 10; i++) {
        if ((i & 1) == 0) {
            System.out.print(i + " ");
        }
    }
    System.out.print("\n奇数：");
    for (int i = 0; i < 10; i++) {
        if ((i & 1) == 1) {
            System.out.print(i + " ");
        }
    }
    System.out.println("\n");

    //位操作运算符(正数变负数，负数变正数)
    int d = 2;
    System.out.println(~d + 1);


    //绝对值(右移31位，0为正数，-1为负数)
    int e = -10;
    int f = e >> 31;
    System.out.println("原值：" + e);
    System.out.println("绝对值：" + (f == 0 ? e : (~e + 1)));
    //优化后
    System.out.println("绝对值：" + ((e ^ f) - f));

    //十进制转二进制
    int m = 10;
    int n = 2;
    System.out.println(toBinaryString(m));
    System.out.println(toBinaryString(n));
    System.out.println(m >> n);
    System.out.println(toBinaryString(m >> n));

}

public static String toBinaryString(int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 31; i >= 0; i--) {
        if ((n & (1 << i)) != 0) {
            sb.append("1");
        } else {
            sb.append("0");
        }
        if ((32 - i) % 8 == 0) {
            sb.append(" ");
        }
    }
    return sb.toString();
}
```

