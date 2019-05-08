## 示例

```java
package com.demo;

public enum TestEnum {

    QQ("QQ应用",0),
    WECHAT("微信应用",1);

    private String name;
    private int value;

    TestEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    TestEnum(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static TestEnum getByValue(int value) {
        for (TestEnum e : TestEnum.values()) {
            if(e.value == value) {
                return e;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(TestEnum.valueOf("QQ").name);
        System.out.println(TestEnum.getByValue(0).value);
    }
}

```
