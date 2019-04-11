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

# git checkout -b dev origin/dev切换到dev分支
$ git checkout -b dev origin/dev
Switched to a new branch 'dev'
Branch 'dev' set up to track remote branch 'dev' from 'origin'.

# 将dev分支文件拉取到本地
$ git pull

```
