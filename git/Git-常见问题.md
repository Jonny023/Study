## 无法提交，错误提示

```
To https://github.com/Jonny023/Grails.git
 ! [rejected]        master -> master (non-fast-forward)
error: failed to push some refs to 'https://github.com/Jonny023/Grails.git'
hint: Updates were rejected because the tip of your current branch is behind
hint: its remote counterpart. Integrate the remote changes (e.g.
hint: 'git pull ...') before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.

```

## 原因分析

* git上有东西没有更新下来

## 解决办法，执行下面命令，再次提交即可

```shell
git pull --rebase origin master
```



```shell
# 获取远端分支信息，你可以看到和本地和远端不同步的地方
git remote show origin

# 远程分支已经被删除，本地同步
git remote prune origin
```

