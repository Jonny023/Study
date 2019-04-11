# 动态

* 动态增加属性
```groovy
public static void main(String[] args) {
    T1 t = new T1()
    t.a = "hello"

    def jsonString = JsonOutput.toJson(t)
    println (jsonString)

    t.metaClass.age = 20

    def jsonString1 = JsonOutput.toJson(t)
    println (jsonString1)

    // 输出
    
    // {"a":"hello"}
    // {"age":20,"a":"hello"}
}
```
