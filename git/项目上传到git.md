### 常用命令行

```
echo "# demo" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/Jonny023/test.git
git push -u origin master
```

或者

```
git remote add origin https://github.com/Jonny023/test.git
git push -u origin master
```

### 查看分支

```
git branch  //查看本地所有分支
git branch -r  //查看远程所有分支
git branch -a   //查看本地和远程所有分支
```

### 创建分支

```
git branch branchName
```

### 创建并切换到分支

```
git checkout -b branchName
```

### 切换分支

```
git checkout branchName
```

### 删除了某个文件，尚未提交到版本库，怎么恢复

```
git checkout -- index.html
```

### 删除了某个文件，且提交到版本库，怎么恢复

```
通过git log找到删除之前的commitId，即105628dcf87b4472fb2e92ef6b1251d263b2185b
git checkout 105628dcf87b4472fb2e92ef6b1251d263b2185b -- index2.html
```

### 文件检出

```
git checkout [-q] [<commit id>] [--] <paths>
```

### 删除文件

```
git rm page.html
git commit -m "删除page.html"
```

### 拉取并合并

```
git fetch
```

### 拉取

```
git pull
```
