### 1、下载`maven-xx.zip`并解压到本地

### 2、新建环境变量
* 变量名：`MVN_HOME`
* 变量值：`D:\maven\apache-maven-3.1.1-bin\apache-maven-3.1.1`

### 3、在系统变量中找到`Path`
* 在他的环境变量值尾部加入：`;%MVN_HOME%\bin;` 前面注意分号

### 4、`dos`环境中，`mvn -v`
* 如果能打印出`maven`的版本号，说明配置成功。

### 5、设置本地仓库配置
> 修改我们仓库地址，仓库用于存放我们项目所依赖的所有jar包。
* 仓库路径：`D:\maven\repo` 这个路径是我自己创建，你可以将路径创建在任何位置。
* 打开`D:\maven\apache-maven-3.1.1-bin\apache-maven-3.1.1\conf`目录（安装目录）下的`setting.xml`文件，设置成我们创建的仓库路径

### 6、配置验证
* 打开命令提示符，输入：`mvn help:system`

### 7、`Myeclipse`配置`maven`插件
* 安装好maven插件后需要在Myeclipse中进行配置，配置过程：打开`Myeclipse`, 进入`window-preference-myeclipse-maven4myeclipse`
* 设置`maven`安装路径
* 在安装路径中选择我们的maven文件，然后“apply”，最后点“OK”

### 8、设置maven当前库
* 在`User Settings`中选择我们`maven`中的`Strings.xml`配置文件。选择后他的`local Repository`会自动变为我们配置文件中设定的路径。

#### 国内仓库

```xml
<mirror>  
    <id>alimaven</id>  
    <mirrorOf>central</mirrorOf>
    <name>aliyun maven</name>  
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
</mirror>
<mirror>  
    <id>huaweicloud</id>  
    <mirrorOf>central</mirrorOf>
    <name>huaweiyun maven</name>  
    <url>http://mirrors.huaweicloud.com/repository/maven/</url>
</mirror>
<mirror>  
    <id>mvnrepository</id>  
    <mirrorOf>central</mirrorOf>
    <name>mvnrepository maven</name>  
    <url>https://mvnrepository.com/artifact/</url>
</mirror>
<mirror>    
    <id>maven2</id> 
    <mirrorOf>central</mirrorOf>	  
    <name>Human Readable Name for this Mirror.</name>    
    <url>http://uk.maven.org/maven2/</url>    
</mirror>
<mirror>    
  <id>ibiblio</id>    
  <mirrorOf>ibiblio</mirrorOf>    
  <name>Human Readable Name for this Mirror.</name>    
  <url>http://mirrors.ibiblio.org/pub/mirrors/maven2/</url>    
</mirror>
```
