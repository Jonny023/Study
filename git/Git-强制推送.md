当我们有时候回滚了代码，想强制push到远程仓库的时候，

```
git push origin --force
```

会报如下错误：

> You are not allowed to force push code to a protected branch on this project


* 如果用的是gitlab版本库，这说明gitlab对仓库启用了保护，需要在仓库中设置一下：

`"Settings" -> "Repository" -> scroll down to "Protected branches".`
