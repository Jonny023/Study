* 提示时区无效
  * 方式1 在Dbeaver.ini中加入-Duser.timezone=GMT+8
  * 方式2 右击快捷方式--属性--目标--在路径后面跟上参数：-Duser.timezone=GMT+8

```bash
C:\Users\Administrator\AppData\Local\DBeaver\dbeaver.exe -nl zh -vmargs -Duser.timezone=GMT+8
```
