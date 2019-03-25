# 基本数据类型计算

* `byte,short,char`类型运算的时候需要强转
  * 运算时自动转为`int`类型，他们的优先级比`int`低
  * 类型转换：`char-->`    自动转换：`byte-->short-->int-->long-->float-->double`

```java
char a = 'a';
char b = 'b';
char c = (char) (a+b);

short d = 0;
short e = 2;
short g = (short) (d+e);

byte x = 1;
byte y = 2;
byte z = (byte) (x * y);

// 无需强转
short m = 10;
int n = m;
```
