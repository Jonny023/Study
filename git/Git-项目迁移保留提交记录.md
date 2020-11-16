> GitBlit迁移到Gitlab保留提交记录

```
cd <项目目录>
git fetch --all
git fetch --tags
git remote rename origin old-origin #可以不保留
git remote add origin http://***(项目的新仓库地址)
#git remote set-url origin <项目的新仓库地址>
git push origin --all 
git push --tags 
有多个分支的话，就切换到另一个分支提交：
git checkout dev(分支名称)
git push origin -all
```
