# nexus私服

## docker版

```bash
# 创建数据目录
mkdir -p /usr/local/nexus/data

# 赋权
chmod -R 777 /usr/local/nexus/data

# 启动 Nexus 容器
docker run -d \
  --name nexus3 \
  --restart always \
  -e TZ=Asia/Shanghai \
  -p 8081:8081 \
  -v /usr/local/nexus/data:/nexus-data \
  sonatype/nexus3
```



## docker-compose版

### 1.创建数据目录

```sh
mkdir -p /usr/local/nexus/data
chmod 777 /usr/local/nexus/data
```

### 2.构建docker-compose.yaml

> cd /usr/local/nexus/
>
> vim docker-compose.yaml

```yaml
version: "3.3"
services:
  nexus:
    image: sonatype/nexus3
    container_name: nexus3
    restart: always
    environment:
      - TZ=Asia/Shanghai
    ports:
      - 8081:8081
    volumes:
      - /usr/local/nexus/data:/nexus-data
```

### 3.运行/停止/重启/删除/删除镜像

```sh
# 运行
docker-compose up -d

# 停止
docker-compose down

# 重启
docker-compose restart

# 删除并清空数据
docker-compose down -v

# 删除容器及数据以及镜像
docker-compose down --rmi all
```

## 配置和使用

### 1.访问

> 首次登陆需要修改密码，密码位置：/nexus-data/admin.password
>
> cat /usr/local/nexus/data/admin.password
>
> 默认用户名：admin，修改后密码我设置的：admin123

[http://192.168.56.101:8081/](http://192.168.56.101:8081/)

### 2.配置使用

> settings.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings>

    <!-- 本地仓库路径 -->
    <localRepository>D:/.m2</localRepository>

    <!-- Nexus 认证 -->
    <servers>
        <server>
            <id>nexus-releases</id>
            <username>admin</username>
            <password>admin123</password>
        </server>
        <server>
            <id>nexus-snapshots</id>
            <username>admin</username>
            <password>admin123</password>
        </server>
    </servers>

    <!-- ====================== 核心 Profile ====================== -->
    <profiles>
        <profile>
            <id>nexus</id>

            <!-- ====================== 1. 下载依赖（你原来的） ====================== -->
            <repositories>
                <repository>
                    <id>maven-public</id>
                    <name>maven-public</name>
                    <url>http://192.168.56.101:8081/repository/maven-public/</url>
                    <releases><enabled>true</enabled></releases>
                    <snapshots><enabled>true</enabled></snapshots>
                </repository>
                <repository>
                    <id>nexus-releases</id>
                    <name>nexus-releases</name>
                    <url>http://192.168.56.101:8081/repository/maven-releases/</url>
                    <releases><enabled>true</enabled></releases>
                    <snapshots><enabled>false</enabled></snapshots>
                </repository>
                <repository>
                    <id>nexus-snapshots</id>
                    <name>nexus-snapshots</name>
                    <url>http://192.168.56.101:8081/repository/maven-snapshots/</url>
                    <releases><enabled>false</enabled></releases>
                    <snapshots><enabled>true</enabled></snapshots>
                </repository>
            </repositories>

            <pluginRepositories>
                <pluginRepository>
                    <id>maven-public</id>
                    <url>http://192.168.56.101:8081/repository/maven-public/</url>
                    <releases><enabled>true</enabled></releases>
                    <snapshots><enabled>true</enabled></snapshots>
                </pluginRepository>
                <pluginRepository>
                    <id>nexus-releases</id>
                    <url>http://192.168.56.101:8081/repository/maven-releases/</url>
                    <releases><enabled>true</enabled></releases>
                    <snapshots><enabled>false</enabled></snapshots>
                </pluginRepository>
                <pluginRepository>
                    <id>nexus-snapshots</id>
                    <url>http://192.168.56.101:8081/repository/maven-snapshots/</url>
                    <releases><enabled>false</enabled></releases>
                    <snapshots><enabled>true</enabled></snapshots>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles>

    <!-- 自动激活 -->
    <activeProfiles>
        <activeProfile>nexus</activeProfile>
    </activeProfiles>
</settings>
```



### 3.上传

#### 3.1 清除lastUpdated文件

> 进入本地maven仓库地址，我的是：d:\\.m2，执行如下脚本

```bat
for /r %i in (*.lastUpdated) do del %i
```

#### 3.2 构建脚本

> 在本地仓库目录

```sh
#!/bin/bash
# copy and run this script to the root of the repository directory containing files
# this script attempts to exclude uploading itself explicitly so the script name is important
# Get command line params
while getopts ":r:u:p:" opt; do
	case $opt in
		r) REPO_URL="$OPTARG"
		;;
		u) USERNAME="$OPTARG"
		;;
		p) PASSWORD="$OPTARG"
		;;
	esac
done
 
find . -type f -not -path './mavenimport\.sh*' -not -path '*/\.*' -not -path '*/\^archetype\-catalog\.xml*' -not -path '*/\^maven\-metadata\-local*\.xml' -not -path '*/\^maven\-metadata\-deployment*\.xml' | sed "s|^\./||" | xargs -I '{}' curl -u "$USERNAME:$PASSWORD" -X PUT -v -T {} ${REPO_URL}/{} ;
```

#### 3.3 上传到nexus

##### 3.3.1 上传所有

> 在本地仓库目录打开git bash命令行

```sh
# ./uploadToNexus.sh -u 你的nexus用户名 -p 你的nexus密码 -r http://127.0.0.1:8081/repository/你的远程仓库名/
./uploadToNexus.sh -u admin -p admin123 -r http://192.168.56.101:8081/repository/maven-releases/
```

##### 3.3.2 上传指定目录jar

> git bash脚本执行

```sh
cd /d/.m2/com/mysql/mysql-connector-j

find . -type f \
  -not -name "*.lastUpdated" \
  -not -name "_remote.repositories" \
  -not -name "resolver-status.properties" \
  | while read file
do
  cleanPath=$(echo "$file" | sed 's|^\./||')
  echo "上传: $cleanPath"

  curl -u "admin:admin123" -X PUT -T "$file" \
  "http://192.168.56.101:8081/repository/maven-releases/com/mysql/mysql-connector-j/$cleanPath"
done
```

##### 3.3.3 上传指定版本jar

> git bash脚本执行

```bash
cd /d/.m2/com/mysql/mysql-connector-j/8.0.33

find . -type f \
  -not -name "*.lastUpdated" \
  -not -name "_remote.repositories" \
  | while read file
do
  cleanPath=$(echo "$file" | sed 's|^\./||')
  echo "上传: $cleanPath"

  curl -u "admin:admin123" -X PUT -T "$file" \
  "http://192.168.56.101:8081/repository/maven-releases/com/mysql/mysql-connector-j/8.0.33/$cleanPath"
done
```

##### 3.3.4 指定文件通用版

> git bash脚本执行

```bash
#!/bin/bash

LOCAL_DIR="/d/.m2/com/mysql/mysql-connector-j"
REMOTE_PREFIX="com/mysql/mysql-connector-j"
REPO_URL="http://192.168.56.101:8081/repository/maven-releases"
USERNAME="admin"
PASSWORD="admin123"

cd "$LOCAL_DIR" || exit 1

find . -type f \
  -not -name "*.lastUpdated" \
  -not -name "_remote.repositories" \
  -not -name "resolver-status.properties" \
  | while read file
do
  cleanPath=$(echo "$file" | sed 's|^\./||')
  uploadUrl="$REPO_URL/$REMOTE_PREFIX/$cleanPath"

  echo "上传: $file"
  echo "目标: $uploadUrl"

  curl -u "$USERNAME:$PASSWORD" -X PUT -T "$file" "$uploadUrl"
done
```

##### 3.3.5 上传项目依赖的jar

> 构建upload-project-jar-to-nexus.sh

```bash
#!/bin/bash

# ==========================================
# 当前项目依赖上传 Nexus 脚本
# 功能：
# 1. 自动读取当前项目依赖
# 2. 仅上传当前项目实际依赖的 jar/pom
# 3. 自动跳过已存在文件
# 4. 自动忽略不存在的依赖目录
# ==========================================

# 本地 Maven 仓库路径
M2="/d/.m2"

# Nexus 仓库地址
REPO_URL="http://192.168.56.101:8081/repository/maven-releases"

# Nexus 账号密码
USERNAME="admin"
PASSWORD="admin123"

echo "=========================================="
echo "开始生成当前项目依赖列表..."
echo "=========================================="

mvn dependency:list "-DincludeScope=runtime" "-DoutputFile=deps.txt"

if [ $? -ne 0 ]; then
    echo "依赖列表生成失败，脚本结束"
    exit 1
fi

if [ ! -f deps.txt ]; then
    echo "未找到 deps.txt，脚本结束"
    exit 1
fi

echo
echo "=========================================="
echo "开始上传依赖到 Nexus..."
echo "=========================================="

grep ':jar:' deps.txt | while read -r line
do
    gav=$(echo "$line" | sed 's/^\[INFO\] *//')

    groupId=$(echo "$gav" | cut -d: -f1)
    artifactId=$(echo "$gav" | cut -d: -f2)
    version=$(echo "$gav" | cut -d: -f4)

    # 跳过空行
    if [ -z "$groupId" ] || [ -z "$artifactId" ] || [ -z "$version" ]; then
        continue
    fi

    groupPath=$(echo "$groupId" | tr '.' '/')
    baseDir="$M2/$groupPath/$artifactId/$version"

    echo
    echo "------------------------------------------"
    echo "处理依赖: $groupId:$artifactId:$version"
    echo "本地目录: $baseDir"
    echo "------------------------------------------"

    if [ ! -d "$baseDir" ]; then
        echo "目录不存在，跳过"
        continue
    fi

    find "$baseDir" -maxdepth 1 -type f \
      \( -name "*.jar" -o -name "*.pom" -o -name "*.sha1" -o -name "*.md5" \) \
      | while read -r file
    do
        relativePath="${file#$M2/}"

        echo "上传文件: $relativePath"

        http_code=$(curl -u "$USERNAME:$PASSWORD" \
          -X PUT \
          -T "$file" \
          -s \
          -o /dev/null \
          -w "%{http_code}" \
          "$REPO_URL/$relativePath")

        if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
            echo "上传成功"
        elif [ "$http_code" = "400" ]; then
            echo "已存在，跳过"
        elif [ "$http_code" = "401" ]; then
            echo "认证失败"
        elif [ "$http_code" = "403" ]; then
            echo "无权限上传"
        elif [ "$http_code" = "404" ]; then
            echo "仓库地址不存在"
        else
            echo "上传失败，HTTP状态码: $http_code"
        fi
    done
done

echo
echo "=========================================="
echo "全部执行完成"
echo "=========================================="
```

### 4.项目发布到nexus

> 在项目的pom.xml中配置distributionManagement，正式版自动传到maven-releases，快照版传到maven-snapshots

```xml
    <build>
        <plugins>
            <!-- 如需上传javadoc源码 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <executions>
                    <execution>
                        <id>attach-javadocs</id>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <!-- ====================== 2. 上传发布（你要加的） ====================== -->
    <distributionManagement>
        <repository>
            <id>nexus-releases</id>
            <url>http://192.168.56.101:8081/repository/maven-releases/</url>
        </repository>
        <snapshotRepository>
            <id>nexus-snapshots</id>
            <url>http://192.168.56.101:8081/repository/maven-snapshots/</url>
        </snapshotRepository>
    </distributionManagement>
```

