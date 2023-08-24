## 普通Maven项目打包jar

[toc]

### 1.maven-shade-plugin (class)

> 打包时把依赖的jar下的所有class(class文件)同时编译到打包的jar中，还可以配置排除不使用的类

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.2.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer
implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>org.example.TestUtil</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 2.maven-assembly-plugin (class)

> 同maven-shade-plugin，都是将(class文件)jar中的依赖一起并入到jar中，不是独立的jar

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.0.0</version>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>org.example.TestUtil</mainClass>
                    </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
            <executions>
                <execution>
                    <id>make-assembly</id> <!-- this is used for inheritance merges -->
                    <phase>package</phase> <!-- 指定在打包节点执行jar包合并操作 -->
                    <goals>
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 3.maven-assembly-plugin (jar)

> 默认打包也是将class合并到最终的jar中，在idea中通过plugins下面的`assembly:assembly`进行打包

```xml
<build>
    <plugins>
        <plugin>
            <artifactId>maven-assembly-plugin</artifactId>
            <configuration>
                <!--这部分可有可无,加上的话则直接生成可运行jar包-->
                <archive>
                    <manifest>
                        <mainClass>org.example.TestUtil</mainClass>
                    </manifest>
                </archive>
            </configuration>
        </plugin>
    </plugins>
</build>
```

#### 3.1 将依赖的jar直接打包到jar中

3.1.1 配置pom.xml

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>2.6</version>
            <configuration>
                <!--这部分可有可无,加上的话则直接生成可运行jar包-->
                <archive>
                    <manifest>
                        <mainClass>org.example.TestUtil</mainClass>
                    </manifest>
                </archive>
                <descriptors>
                    <descriptor>src/main/assemble/assembly-descriptor.xml</descriptor>
                </descriptors>
            </configuration>
            <executions>
                <execution>
                    <id>build-jar</id>
                    <phase>package</phase>
                    <goals>
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.0.2</version>
            <configuration>
                <archive>
                    <addMavenDescriptor>false</addMavenDescriptor>
                    <manifest>
                        <!-- 是否要把第三方jar放到manifest的classpath中 -->
                        <addClasspath>true</addClasspath>
                        <!--生成的manifest中classpath的前缀，因为要把第三方jar放到lib目录下，所以classpath的前缀是lib/-->
                        <classpathPrefix>lib/</classpathPrefix>
                        <mainClass>org.example.TestUtil</mainClass>
                    </manifest>
                </archive>
            </configuration>
        </plugin>
    </plugins>
</build>
```

3.1.2 在`src/main/assemble`下创建`assembly-descriptor.xml`

> 打包后压缩文件名字以`-assembly`结尾，也就是下面定义的id

```xml
<assembly>
    <id>assembly</id>
    <formats>
        <format>zip</format>
    </formats>

    <includeBaseDirectory>true</includeBaseDirectory>

    <fileSets>
        <fileSet>
            <directory>src/main/bin</directory>
            <includes>
                <include>*.sh</include>
            </includes>
            <outputDirectory>bin</outputDirectory>
            <fileMode>0755</fileMode>
        </fileSet>
        <fileSet>
            <directory>src/main/conf</directory>
            <outputDirectory>conf</outputDirectory>
        </fileSet>
        <fileSet>
            <directory>src/main/sql</directory>
            <includes>
                <include>*.sql</include>
            </includes>
            <outputDirectory>sql</outputDirectory>
        </fileSet>
        <fileSet>
            <directory>target/classes/</directory>
            <includes>
                <include>*.properties</include>
                <include>*.xml</include>
                <include>*.txt</include>
            </includes>
            <outputDirectory>conf</outputDirectory>
        </fileSet>
    </fileSets>

    <files>
        <file>
            <source>target/${project.artifactId}-${project.version}.jar</source>
            <outputDirectory>.</outputDirectory>
        </file>
    </files>

    <dependencySets>
        <dependencySet>
            <unpack>false</unpack>
            <scope>runtime</scope>
            <outputDirectory>lib</outputDirectory>
        </dependencySet>
    </dependencySets>
</assembly>
```

3.1.2 重新打包

### 4.maven-jar-plugin (jar)

> 在target目录下生成项目jar，所有依赖的jar都存放到lib目录下，需要将jar和lib目录同时拷贝才能运行

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.2.0</version>
            <configuration>
                <archive>
                    <manifest>
                        <addClasspath>true</addClasspath>
                        <useUniqueVersions>false</useUniqueVersions>
                        <classpathPrefix>lib/</classpathPrefix>
                        <mainClass>org.example.TestUtil</mainClass>
                    </manifest>
                    <!--<manifestEntries>  &lt;!&ndash;将ojdbc8-1.0.jar写进MANIFEST.MF文件中的Class-Path&ndash;&gt;
                            <Class-Path>lib/ojdbc8-1.0.jar</Class-Path>
                        </manifestEntries>-->
                </archive>
                <excludes>   <!--排除用于测试的日志配置资源文件-->
                    <exclude>log4j2-test.xml</exclude>
                </excludes>
            </configuration>
        </plugin>
        <!--在打包阶段将依赖的jar包导出到lib目录下-->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <executions>
                <execution>
                    <id>copy-dependencies</id>
                    <phase>package</phase>
                    <goals>
                        <goal>copy-dependencies</goal>
                    </goals>
                    <configuration>
                        <type>jar</type>
                        <includeTypes>jar</includeTypes>
                        <outputDirectory>${project.build.directory}/lib</outputDirectory>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

> 以上几种打包方式是我亲测可以运行的，若有其他方式，烦请告知！

## 问题

#### 多模块打包问题

> Could not resolve dependencies for project，多模块打包找不到依赖，需要先将被依赖的模块`install`到本地，再执行`package`打包

### 作用于

> 打包时，如果依赖的scope为test,provided时不会被打包到jar中

compile: 无论是多模块还是直接依赖打包都会存在

多模块：

* 若父工程依赖的scope作用域为test,runtime，则不能向下传递，也就是说子模块在打包时无法引用父工程依赖的Java类
* provided: 在打包时会移除，父工程引入了，子工程可以测试运行

非多模块：

* provided: 打包后包里面存在jar依赖文件，只是描述文件`MANIFEST.MF`中移除掉了

* test/runtime: 在类中都无法引用，编译无法通过

## 指定配置文件打包

```xml
mvn clean package -s "D:\dev_tools\apache-maven-3.3.9\conf\settings - copy.xml"
```
