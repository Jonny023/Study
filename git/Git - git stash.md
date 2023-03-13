## git stash 

> git stash暂存文件被clear

```shell
# 找出刚才删除的提交对象和文件对象, 里面是一个一个的 dangling commit commitId
git fsck --lost-found

# 一个一个的查看修改的内容, 找到了我们需要恢复的commitId之后
git show commitId

# 直接恢复
git stash apply commitId
```
