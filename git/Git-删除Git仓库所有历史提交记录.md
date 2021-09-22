# 删除Git仓库所有历史提交记录

### 1.设置用户

```shell
git config --global user.name "Jonny"
git conifg --global user.email "xxx@qq.com"
```

### 1. 首先是在当前项目中，切换到本地一个新的分支（分支名随意）

```shell
git checkout --orphan latest_branch
```

命令最后的latest_branch可以根据自己喜好进行修改（如果修改过，后续其他命令同样的地方记得也要修改下）。

### 2. 提交当前项目中，所有的文件到上一步切换的分支中

```shell
git add -A
```

### 3. 提交上一步的修改

```shell
git commit -am "commit message"
```

其中commit message就是你的Git提交说明，可以自行修改。

### 4. 删除原有的master分支

```shell
git branch -D master
```

如果需要删除历史记录的分支不是master，则修改最后的master即可。

### 5. 重命名当前切换的分支为master

```shell
git branch -m master
```

同样的，如果第4步中，删除的不是master分支，则需要修改为对应的分支名。

### 6. 最后一步，强制推送到线上的master分支

```shell
git push -f origin master
```

完成上诉步骤后，Git仓库中对应的master分支中的所有历史记录就全部删除了，只会存在第3步中，唯一的一次提交记录。