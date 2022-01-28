## js对url进行转码

> encodeURI、encodeURIComponent、decodeURI、decodeURIComponent四个用来编码和解码 URI 的函数之外 ECMAScript 语言自身不提供任何使用 URL 的支持。

* encodeURI只转换非域名部分
* encodeURIComponent 会对整个url进行编码

| url                     | encodeURI                             | encodeURIComponent                        |
| ----------------------- | ------------------------------------- | ----------------------------------------- |
| www.test.com?q=Java编程 | www.test.com?q=Java%E7%BC%96%E7%A8%8B | www.test.com%3Fq%3DJava%E7%BC%96%E7%A8%8B |

