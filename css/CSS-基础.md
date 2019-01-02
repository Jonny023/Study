#CSS层叠样式表
##CSS使用方法
* 在元素的`style`属性内  行内式 内联式
* 写在 `<style>` 标签内
* 写到外部css文件中，用`<link>`关联到HTML中，  
* `@import`可以引入css，用于css中引入css    

## CSS的格式
    
    选择器{
        属性:值;
        属性:值;
    }
###CSS长度单位
* `px`：绝对单位，页面按精确像素展示
* `em`：相对单位，基准点为父节点字体的大小，如果自身定义了`font-size`按自身来计算（浏览器默认字体是`16px`），整个页面内`1em`不是一个固定的值。
* `百分比`
* `pt(磅)  cm(厘米)  mm(毫米)`  绝对单位
* `rem`：相对单位，可理解为`root em`, 相对根节点html的字体大小来计算，CSS3新加属性，`chrome/firefox/IE9+`支持。
* `vw：viewpoint width`，视窗宽度，`1vw`等于视窗宽度的`1%`。
* `vh：viewpoint height`，视窗高度，`1vh`等于视窗高度的`1%`。
* `vmin：vw`和`vh`中较小的那个。
* `vmax：vw`和`vh`中较大的那个。

###颜色表示
* 颜色英文
* `RGB（X,Y,Z）`数字`（0~255,0~255,0~255）`或`百分比`。只要三个都一样，就是灰色
* 16进制
    * #ff0000  红色  可以简写 #f00
    * #00ff00  绿色  简写  #0f0
    * #0000ff   蓝色  简写 #00f
    * #ffffff    白色    简写 #fff
    * #000000  黑色  简写 #000
    * #cccccc   灰色  简写 #ccc
    * #336699  简写  #369

###CSS注释
    /*注释*/

##CSS选择器
    <!--引号表示元素可以变更-->
* 元素选择器 
    
        p {}
        div {}
* `ID`选择器

        #mylist｛｝
        #myitem｛｝
            
        <p id="mylist"></p>
        选择 id="mylist" 的所有元素。

* `Class`选择器

        .'text'{}
        .'div1'{}
        
        <div class="div1"></div>
        选择 class="div1" 的所有元素

* 全局选择器

        *{}
        选择所有元素
* 关联选择器
        
        div,p 选择所有div元素和p元素
        div p 选择div元素内的所有p元素
        div>p 选择父元素为div的所有p元素
        div+p 选择紧接在div元素后的所有p元素
        ['target']    选择带有 target 属性所有元素。
        ['target'='_blank']    选择 target="_blank" 的所有元素。
        ['title'~='flower']    选择 title 属性包含单词 "flower" 的所有元素。

* 伪类选择器

        <!--引号表示元素可以变更-->
        a:link    选择所有未被访问的链接。
        a:visited    选择所有已被访问的链接。
        a:active    选择活动链接。
        a:hover    选择鼠标指针位于其上的链接。
        input:focus    选择获得焦点的 input 元素。

* 组合选择器
	* 两种基本选择器的组合
	
* 选择器详细
#### http://www.w3school.com.cn/c***ef/css_selectors.asp

###选择器优先级
* `ID>CLASS>Tag`
* 组合选择器  `ID`权重`100`  `class`权重`10`  `Tag`权重`1`
* `!important` 重要
*  首先遵循就近原则，无视`!important`

##CSS属性
###字体属性
* `font-family`    衬线字体（`serif`）  非衬线字体(`sans-serif`)
    
        font-family: "字体名字" 是否是衬线字体;

* `font-style`       字体风格  `normal`(默认)/`itailc`(斜体)/`oblique`(强制变斜)
        
        font-style: normal;
        font-style: oblique;
        font-style: italic;
* `font-weight`  字体粗度    `normal`(默认)/`bold`(加粗)/`bodler`(更粗)/`lighter`(细)/数字(>=600 粗)

        font-weight: normal;
        font-weight: bold;
        font-weight: lighter; 
        font-weight: 600;
        font-weight: bolder;
* `font-variant`  字变形  `noraml`(默认)/`small-caps`(小型大写字母)

        font-variant: small-caps;
        font-variant: noraml;
* `font-size`     字体大小
* `font`  统一设置 字体需要加双引号，其他无需

        font:[style/weight/variant] size family

### 颜色属性
* `color`   值：`字母/rgb/16进制`

### 文本属性
* `border:1px solid #ccc;` 边框粗细及颜色
* `width：100px;` 宽度
* `height:100px;` 高度
* `background-color:;`背景颜色
* `color:;`字体颜色

        /*字母间距*/
        letter-spacing: 0px;
            
        /*单词边距*/
        word-spacing: 0px;
    
        /*文字修饰*/
        text-decoration: none;  无线
        text-decoration: underline; 下划线
        text-decoration: overline;  上划线
        text-decoration: line-through; 中划线，删除线
