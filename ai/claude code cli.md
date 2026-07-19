# claude code cli

## 1.安装

```sh
# powershell安装
irm https://claude.ai/install.ps1 | iex

# powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned

# node npm安装
npm install -g @anthropic-ai/claude-code
# 加速镜像安装
npm install -g @anthropic-ai/claude-code --registry=https://registry.npmmirror.com/

# 验证版本
claude –version
```

## 2.跳过登陆检查

```shell
# linux
echo '{"hasCompletedOnboarding": true}' > ~/.claude.json

# windows
echo '{"hasCompletedOnboarding": true}' > $env:USERPROFILE\.claude.json
```

## 3.配置环境变量

```sh
# C:\Users\你的用户名\.local\bin\
%userprofile%\.local\bin

# 复制运行得到的路径，配置环境变量到PATH中
```

## 4.配置API

> 查看配置

```sh
# 用subline编辑
subl %userprofile%/.claude.json
# 用windows自带记事本编辑
notepad %userprofile%/.claude.json
# 用notepad++编辑
notepad++ %userprofile%/.claude.json

# 编辑配置
subl %userprofile%/.claude/settings.json
```

> 编辑配置

```sh
# 创建配置目录
mkdir -p ~/.claude

# 创建 settings.json
cat > ~/.claude/settings.json << 'EOF'
{
  "env": {
    "ANTHROPIC_AUTH_TOKEN": "你的_API_KEY",
    "ANTHROPIC_BASE_URL": "你的_URL",
    "ANTHROPIC_MODEL": "模型名称，在你申请的大模型上面就可以看到"
  }
}
EOF

# 创建 .claude.json
cat > ~/.claude.json << 'EOF'
{
  "hasCompletedOnboarding": true
}
EOF
```

## 5.禁用自动更新

```json
"DISABLE_AUTOUPDATER": "1",
"autoUpdates": false,
"ANTHROPIC_CLI_NO_UPDATE_CHECK": "1",
```

