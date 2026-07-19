## claude

> .claude.json修改配置选择国内模型%userprofile%/.claude.json

```json
"hasCompletedboarding": true,

// 或（不通版本可能不一样）
"hasCompletedOnboarding": true,
```

## 配置api

```sh
SETX ANTHROPIC_API_KEY "sk-xxx"
SETX ANTHROPIC_BASE_URL "https://api.deepseek.com/anthropic"
SETX ANTHROPIC_MODEL "deepseek-v4-flash"

# 查看配置是否成功
echo %ANTHROPIC_API_KEY%
echo %ANTHROPIC_BASE_URL%
echo %ANTHROPIC_MODEL%

# 设置空
set ANTHROPIC_AUTH_TOKEN=
set ANTHROPIC_API_KEY=

# 清空残留信息
claude logout

# powershell移除环境变量
Remove-Item Env:ANTHROPIC_AUTH_TOKEN -ErrorAction SilentlyContinue
Remove-Item Env:ANTHROPIC_API_KEY -ErrorAction SilentlyContinue
Remove-Item Env:ANTHROPIC_BASE_URL -ErrorAction SilentlyContinue
Remove-Item Env:ANTHROPIC_MODE -ErrorAction SilentlyContinue

# 查看环境变量
gci env:ANTHROPIC*
```

