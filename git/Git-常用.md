# 常用

要点：

1. git add 要提交的文件
2. 在Staged中的文件如果修改后没有git add，直接执行git commit是无法提交的，可以用`git commit -a`或者先`git add`再`git commit`
3. 慎重`git rm`命令，就算不加 -f 也是会删除本地文件，加 -f 只是在文件在Modifeid的时候可以强制删除，恢复时需先`git reset HEAD <file>`再`git checkout <file>`

- **撤销**

  * git reset HEAD <file> 撤销指定文件在Staged的状态修改
  * --mixed : 不删除工作空间改动代码，撤销commit，并且撤销git add
  * --soft : 不删除工作空间改动代码，撤销commit，不撤销git add
  * --hard: 删除工作空间改动代码，撤销commit，撤销git add（该命令不能带具体文件，直接恢复工作区）

  - git revert HEAD 撤销前一次 commit
  - git revert commit 撤销指定的版本，撤销也会作为一次提交进行保存。
  - git revert是提交一个新的版本，将需要revert的版本的内容再反向修改回去

  

- git revert和 git reset的区别 

  1. git revert是用一次新的commit来回滚之前的commit，git reset是直接删除指定的commit。 
  2. 在回滚这一操作上看，效果差不多。但是在日后继续merge以前的老版本时有区别。因为git revert是用一次逆向的commit“中和”之前的提交，因此日后合并老的branch时，导致这部分改变不会再次出现，但是git reset是之间把某些commit在某个branch上删除，因而和老的branch再次merge时，这些被回滚的commit应该还会被引入。 
  3. git reset 是把HEAD向后移动了一下，而git revert是HEAD继续前进，只是新的commit的内容和要revert的内容正好相反，能够抵消要被revert的内容。

### 分支

- 查看所有分支：`git branch`
- 查看所有远程分支：`git branch -r`
- 查看所有本地和远程分支：`git branch -a`
- 新建分支，留在当前分支：`git branch <branch-name>`
- 删除分支：`git branch -d <branch-name>`
- 新建分支，切换到新分支：`git checkout -b <branch-name>`
- 切换到主分支：`git checkout master`
- 推送分支到远程仓库：`git push origin <branch-name>`

### 贮藏

> 当你在一个分支上开发，但是需要切换另一分支上去修改东西时可以使用`git stash`把修改的东西暂时隐藏起来，切换回来后再通过`git stash pop`回复，因为新增的东西没被git托管，如果不执行贮藏操作，切换大另一个分支时会把新增的东西也展示出来。

- 快速贮藏：`git stash`
- 查看所有贮藏列表：`git stash list`
- 应用贮藏：`git stash apply`
- 应用并移除：`git stash pop`

### 标签

- 列出标签：`git tag`
- 显示标签信息：`git show v0.1.2`
- 创建标签：`git tag v0.1.2`
- 创建标签（特定历史节点）：`git tag -a v0.1.1 19c8206 -m 'v0.1.1'`，版本号只需要前 7 位就可以了（19c82066a51151ab70b99fbefb874fee96236b56），位数不重要，只要唯一就可以了。
 - 打历史标签时 `-m （--message=<msg>）`是必须的。
 - 查看历史提交信息：`git log --pretty=oneline`
- 提交标签到版本库：`git push origin v0.1.2`
- 提交标签到版本库（批量）：`git push origin --tags`
- 检出标签：`git checkout -b <branchname> <tagname>`:
  - `git checkout -b b-v0.1.0 v0.1.0`， 不能创建已经存在的分支。这是只是创建本地仓库的一个新分支，并对应到特定的标签而已。

## QA

1. 开发过程中需要切换到另一分支开发，但不想修改或新增的东西在另一个分支上展示出来，想保证代码为拉取的初始状态
   **git stash 保存工作空间的改动，并将代码恢复到改动前**
   **切换分支工作，待修改完成后切换回原分支，git stash pop 恢复之前保存的代码**
2. 如何撤销commit
   **可以用git log查看commit的记录**
   **git reset --soft HEAD~1 使HEAD指向上一次提交，并使本地代码不回退**
3. 如何撤销push
   **情况1：如果本地改动的代码需要保留 git reset --soft HEAD~1**
   **情况2：如果本地的代码需要回退 git reset --hard HEAD~1**
   **以上情况由于更改了版本，需使用 git push -f 强制推送，如果分支是Protected的，则不能 强制推送，需取消对分支的Protected（不建议经常使用）**
4. 已经git rm -f 的文件如何恢复
   **git reset HEAD <file>**
   **git checkout <file>**
5. 如何删除远程仓库中的文件或文件夹，并且不再追踪该文件或文件夹
   **git rm --cached <file>**
   **git commit -m "delete message"**
   **git push**