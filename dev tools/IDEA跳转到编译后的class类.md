### idea跳转到编译后的class类



Settings > Tools > External Tools > Add

#### Tool Settings

| Program           | `C:\Program Files\JetBrains\IntelliJ IDEA 2024.1\bin\idea64.exe` |
| ----------------- | ------------------------------------------------------------ |
| Arguments         | `$FileNameWithoutExtension$.class`                           |
| Working directory | `$OutputPath$\$FileDirRelativeToSourcepath$`                 |



### 使用

* 选中java类，右键>External Tools