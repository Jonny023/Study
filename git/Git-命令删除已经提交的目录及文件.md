> 通过.gitignore删除已经提交的文件 

```
git rm --cached `git ls-files -i --exclude-from=.gitignore` 
git commit -m '移除忽略文件' 
git push origin master

```

> 已经提交

```
git rm -r --cached .
git add .
git commit -m 'update .gitignore'
git push -u origin master
```
