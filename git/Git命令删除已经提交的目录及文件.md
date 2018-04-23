git rm --cached `git ls-files -i --exclude-from=.gitignore` 
git commit -m '移除忽略文件' 
git push origin master
