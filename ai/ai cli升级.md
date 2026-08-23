## cli升级

```sh

npm uninstall -g @anthropic-ai/claude-code

rmdir /s /q D:\dev_tools\node\node_global\node_modules\@anthropic-ai

npm install -g @anthropic-ai/claude-code

# 查看 npm 全局安装路径
npm root -g

# 全局安装claude-code
npm install -g @anthropic-ai/claude-code --registry=https://registry.npmmirror.com/ --foreground-scripts
# 安装codegraph
npm install -g @colbymchenry/codegraph  --registry=https://registry.npmmirror.com/ --foreground-scripts

# 查看仓库最新claude版本
npm view @anthropic-ai/claude-code version --registry=https://registry.npmmirror.com/

# 安装指定版本的claude
npm install -g @anthropic-ai/claude-code@2.1.236 --registry=https://registry.npmmirror.com/ --foreground-scripts

# 查看codegraph版本
npm view @colbymchenry/codegraph version --registry=https://registry.npmmirror.com/

# 查看opencode版本
npm view opencode-ai version --registry=https://registry.npmmirror.com/

# 查看codex版本
npm view @openai/codex version --registry=https://registry.npmmirror.com/

```

