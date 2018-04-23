### 序言：

无论是java、groovy、php等都是静态语言，是指程序在运行时可以改变其结构。html属于静态语言，在编译时变量的数据类型即可确定的语言。

在开发过程中，需求、应用场景的不同，我们选择的开发语言也各不相同，比如我们要开发手机客户端的h5页面，我们大多情况下会选择静态html，尽管用动态标签也可以实现，但是有时候我们是通过手机客户端进行访问，手机端不支持session，那么我们只能选择html来进行数据展示。

那么，在grails中如何访问静态的html页面呢？笔者通过多方面查询资料，终于找到了三种方式（这里以grails为例），分别是：

```
1、将静态html文件存放到grails-app/assets文件夹下任意一个目录下，访问的方式：http://localhost:8080/项目名/assets/index.html

2、在src/main下新建一个resources，然后在resources下面新建一个static或者public文件夹，然后将静态html文件放到此目录下面，这里新建了一个app的目录

3、在src/main下新建一个webapp，然后将静态文件放到webapp下面

```

说明：
```
1、在grails3中，访问静态文件会默认在访问路径中加入一个static的路径，如需去掉这个static，可以在grails-app/conf/application.yml中最后面加入配置：

---
#访问静态文件不用加static
grails:
      resources:
                pattern: '/**'
                
2、方法二中添加两个目录名字只能为public或static，英文这是grails3默认的，修改为其他的就不能访问了

3、方式2和3访问方式，如果配置了application.yml，访问方式为：http://localhost:8080/项目/index.html，
  否则为：http://localhosot:8080/项目/static/index.html或http://localhost:8080/项目/public/index.html
```
