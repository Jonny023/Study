## url匹配

* `/*` 匹配一级
* `/**`匹配多级

```java
import org.springframework.util.AntPathMatcher;

public class Test {

    @org.junit.Test
    public void exe() {
        org.springframework.util.AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/user/**", "/user/list");
        boolean match1 = antPathMatcher.match("/user/**", "/user/list?offset=0");
        boolean match2 = antPathMatcher.match("/user/list/*", "/user/list/10001");
        System.out.println(match);
        System.out.println(match1);
        System.out.println(match2);
    }
}
```

