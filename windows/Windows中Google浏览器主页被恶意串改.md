> 当我们打开浏览器的时候它自动打开了某某网站的首页，这个时候我们如果需要重新配置自己的主页，或者去掉它设置的主页，该怎么办呢？

### 1、浏览器地址栏目输入`chrome://version`，查看命令行看看是否正常

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2018/1109/cbdaababda78424dbd8b02b1908e69d4.png)

> 很显然上图中的浏览器首页被篡改了，指向了后面的http地址

### 2、通过Google浏览器设置查看启动时是否指定了http开头的地址

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2018/1109/7a5eb36d897f41359beae21fc9bf18b3.png)

> 你可以通过这个设置来修改，若无效，请接着往下看

### 3、打开Google浏览器目标位置，复制一份重命名，然后右键-发送到桌面快捷方式

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2018/1109/2d296e7f4e464332a247ec0d0082feeb.png)

### 4、双击打开测试看看，主页是不是进入到了你设置页面，若不是

### 5、选中刚刚发送到桌面的快捷方式--单机右键--属性--目标--最后面先一个空格，然后加上命令行：

```bash
 --flag-switches-begin --flag-switches-end
```

#### 最后查看，已经修改过来了

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2018/1109/68bb418c8e8d42c8ab3c1d8424c58d29.png)



