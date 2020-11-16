### No.1

> You are not allowed to force push code to a protected branch on this project

当我们有时候回滚了代码，想强制push到远程仓库的时候，

```
git push origin --force

# 或git push -f
```

* 如果用的是gitlab版本库，这说明gitlab对仓库启用了保护，需要在仓库中设置一下：

`"Settings" -> "Repository" -> scroll down to "Protected branches".`

### 问题二 

> hint: Updates were rejected because the tip of your current branch is behind
hint: its remote counterpart. Integrate the remote changes (e.g.
hint: 'git pull ...') before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.

```
git pull origin master --allow-unrelated-histories
```
