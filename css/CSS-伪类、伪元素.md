# 实例代码

> css

```css
#demo:before {
	content: open-quote;
	color:red;
}
#demo:after {
	content: close-quote;
	color:red;
}
```

> html

```html
<a id="demo">
    hello,this is a test.
</a>
```

> 效果

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0102/3de21722912f454581d6a8a69ca9c617.png)


## 稍作修饰

> css

```css
#demo:before {
	content: "<";
	color: #fff;
	background: #1E9FFF;
	display: inline-block;
	width: 25px;
	height: 25px;
	cursor: pointer;
	text-align: center;
	border-radius: 100%;
}

#demo:after {
	content: ">";
	color: #fff;
	background: #1E9FFF;
	display: inline-block;
	width: 25px;
	height: 25px;
	cursor: pointer;
	text-align: center;
	border-radius: 100%;
}
```

> 效果

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0102/579fbbfcca8e4a71b16877326629c015.png)

# 注意
* `:before`-内容前加上`“`，`:after`-内容后加上`”`
* 还可以设置为背景图像
* 兼容`IE8+`

## 伪类

|伪类|作用|
|--|--|
|:hover          |鼠标悬停时显示的效果|
|:link           |链接在正常情况下（即页面刚加载完成时）显示的效果|
|:active         |当元素处于激活状态（鼠标在元素上按下还没有松开）时所显示的效果|
|:visited        |链接被点击后显示的效果|
|:first-child    |选择器匹配属于其父元素的首个子元素的指定选择器|
|:last-child     |选择器匹配属于其父元素的最后一个子元素的指定选择器|
|:nth-child(n)   |选择器匹配属于其父元素的第 N 个子元素，不论元素的类型|
|:not()          |匹配非指定选择器的每个元素（eg. 匹配所有类名不为active的p元素 p:not(".active")）|
|:focus          |元素获得光标焦点时的效果（eg. input 输入框 input:focus）

## 伪元素

|伪元素|示例|作用|
|--|--|--|
|:first-letter|	p:first-letter|	选择每个<p> 元素的第一个字母|
|:first-line|	p:first-line|	选择每个<p> 元素的第一行|
|:first-child|	p:first-child|	选择器匹配属于任意元素的第一个子元素的 <]p> 元素|
|:before|	p:before|	在每个<p>元素之前插入内容|
|:after	|p:after|	在每个<p>元素之后插入内容|
|:lang(language)|	p:lang(it)	|为<p>元素的lang属性选择一个开始值|

