## node安装codex cli

> 配合cc switch使用

```sh
# 有网环境执行
mkdir codex

cd codex

npm init -y

# 安装codex
npm install @openai/codex @colbymchenry/codegraph --registry=https://registry.npmmirror.com/

# node_modules 文件夹打包压缩
# PowerShell 命令，也可以手动打包
Compress-Archive -Path .\codex -DestinationPath .\codex.zip


# 升级
rm -rf node_modules package-lock.json
npm install @openai/codex@latest @colbymchenry/codegraph@latest --registry=https://registry.npmmirror.com
```

