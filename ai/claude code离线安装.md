## 公网安装

```sh
# 有网环境执行
mkdir claudecode

cd claudecode

npm init -y

# 安装claude code
npm install @anthropic-ai/claude-code @colbymchenry/codegraph --registry=https://registry.npmmirror.com/

# node_modules 文件夹打包压缩
# PowerShell 命令，也可以手动打包
Compress-Archive -Path .\claudecode-offline -DestinationPath .\claudecode-offline.zip
```

## 内网安装

### 1.解压claudecode-offline.zip

- `node_modules\@anthropic-ai\claude-code\` —— 主包
- `node_modules\@anthropic-ai\claude-code-win32-x64\` —— Windows 可执行文件

### 2.配置环境

```sh
D:\claudecode-offline\node_modules\.bin
D:\claudecode-offline\node_modules\@anthropic-ai\claude-code-win32-x64

# 验证 claude 可用
claude --version
# 验证 codegraph 离线命令
codegraph --help
```

### 3.关闭上报环境变量（提升本地推理速度）

```sh
# Windows 临时生效
$env:CLAUDE_CODE_ATTRIBUTION_HEADER="0"
# 永久设置（管理员终端）
[Environment]::SetEnvironmentVariable("CLAUDE_CODE_ATTRIBUTION_HEADER", "0", "User")
```



### 3.跳过登陆检查

```shell
# linux
echo '{"hasCompletedOnboarding": true}' > ~/.claude.json

# windows
echo '{"hasCompletedOnboarding": true}' > $env:USERPROFILE\.claude.json
```