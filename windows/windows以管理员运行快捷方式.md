### Windows通过右键菜单，快捷打开CMD(Admin)窗口

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2018/1022/3c3393038d29453cbd1c1a09e9242875.png)

>   创建一个后缀为.reg的文件，将下面的内容复制复制到该文件中

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Windows Registry Editor Version 5.00
; Created by: Shawn Brink
; http://www.sevenforums.com
; Tutorial: http://www.sevenforums.com/tutorials/47415-open-command-window-here-administrator.html
[-HKEY_CLASSES_ROOT\Directory\shell\runas]
[HKEY_CLASSES_ROOT\Directory\shell\runas]
@="Open CMD"
"HasLUAShield"=""
[HKEY_CLASSES_ROOT\Directory\shell\runas\command]
@="cmd.exe /s /k pushd \"%V\""
[-HKEY_CLASSES_ROOT\Directory\Background\shell\runas]
[HKEY_CLASSES_ROOT\Directory\Background\shell\runas]
@="Open CMD"
"HasLUAShield"=""
[HKEY_CLASSES_ROOT\Directory\Background\shell\runas\command]
@="cmd.exe /s /k pushd \"%V\""
[-HKEY_CLASSES_ROOT\Drive\shell\runas]
[HKEY_CLASSES_ROOT\Drive\shell\runas]
@="Open CMD"
"HasLUAShield"=""
[HKEY_CLASSES_ROOT\Drive\shell\runas\command]
@="cmd.exe /s /k pushd \"%V\""
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

>   最后双击添加到注册表中
