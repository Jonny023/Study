# 切换分支，拉取文件

```bash
# 通过git branch查看当所在前分支（本地分支），当前在master分支上
$ git branch
* master

# 通过git branch -a 查看所有分支
$ git branch -a
* master
  remotes/origin/HEAD -> origin/master
  remotes/origin/dev
  remotes/origin/master

# git checkout -b dev origin/dev拉取远程分支dev到本地
# 多次拉取会提示已经存在
$ git checkout -b dev origin/dev
Switched to a new branch 'dev'
Branch 'dev' set up to track remote branch 'dev' from 'origin'.

# 将dev分支文件拉取到本地
$ git pull

```

## 切换分支

```bash
# 切换到dev分支
git checkout dev
```

## 删除分支命令

------

删除一条分支：

> git branch -D branchName

删除当前分支外的所有分支：

> git branch | xargs git branch -d

删除分支名包含指定字符的分支：

> git branch | grep 'dev*' | xargs git branch -d

## 分支重命名

```shell
git remote rename origin xxx
```

