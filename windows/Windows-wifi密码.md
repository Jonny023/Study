* 查看wifi密码

```bash
# 查看电脑连接过的所有wifi
netsh wlan show profiles
# 查看wifi信号为Aaron的密码
netsh wlan show profiles name="wifi名称" key=clear

netsh wlan show profiles name="wifi名称" key=clear | findstr "关键内容"

# CMD一键获取 所有连接过的WIFI密码
for /f "skip=9 tokens=1,2 delims=:" %i in ('netsh wlan show profiles') do @echo %j | findstr -i -v echo | netsh wlan show profiles %j key=clear
```

