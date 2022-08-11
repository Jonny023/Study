## Python操作excel

* 指定安装版本

```shell
pip install xlrd==1.2.0
```

### 读取excel

```python
# coding=utf-8
from datetime import time

import xlrd

filename = 'C:/Users/fangkeyj/Desktop/temp/student.xlsx'
# xlrd.Book.encoding = 'gbk'
book = xlrd.open_workbook(filename)

# 读取excel第一个sheet
sheet = book.sheets()[0]
# sheet = book.sheet_by_name("Sheet1")

# 总数据行数
row_len = sheet.nrows

print("读取指定单元格：", sheet.cell(1, 0).value)
for i in range(row_len):
    row_data = sheet.row_values(i)
    print("\n所有数据, 行：{}, 数据：{}".format(i, sheet.row_values(i)))
    for cell, row in enumerate(row_data):
        # print(cell, row)
        print(row)
        print('{}: {}'.format(sheet.cell(0, cell).value, row))


        
        
        
        
        
        
###########################
# 判断数据类型

# coding=utf-8
from datetime import datetime

import xlrd
from xlrd import xldate_as_tuple

filename = 'C:/Users/fangkeyj/Desktop/temp/student.xlsx'
book = xlrd.open_workbook(filename)

# 读取excel第一个sheet
sheet = book.sheets()[0]
# sheet = book.sheet_by_name("Sheet1")

# 总数据行数
row_len = sheet.nrows
cell_len = sheet.ncols

print("读取指定单元格：", sheet.cell(1, 0).value)
for row_index in range(1, row_len):
    for cell_index in range(cell_len):
        cell = sheet.cell_value(row_index, cell_index)

        # ctype： 0 empty,1 string, 2 number, 3 date, 4 boolean, 5 error
        ctype = sheet.cell(row_index, cell_index).ctype

        # 日期类型
        if ctype == 3:
            date = datetime(*xldate_as_tuple(cell, 0))
            cell = date.strftime('%Y-%m-%d')
            print(cell)
        # 字符串
        elif ctype == 1:
            print(cell)
        else:
            print(cell)
            pass

```


## 导出excel

* pip install openpyxl

```python
# coding=utf-8
import openpyxl as openpyxl
from openpyxl.styles import Font, PatternFill, Alignment, Side, Border

data = [('ID', '姓名', '年龄', '语文'), (1, '张三', 20, 80), (2, '李四', 22, 82), (3, '王五', 19, 78),
        (4, '小文', 18, 99)]
wb = openpyxl.Workbook()
# ws = wb.create_sheet("学生列表")
ws = wb.active
print(ws.title)
ws.title = u'学生信息'

ws.row_dimensions[1].height = 28
ws.sheet_properties.tabColor = "1072BA"

# 列数
clen = len(data[0])
# 行数
rlen = len(data)

# thin-细线 thick-粗线
bd = Side(style='thin', color="808080")

for row in range(0, rlen):
    for cell in range(0, clen):
        # column和row必须大于等于1
        dom = ws.cell(column=cell + 1, row=row + 1, value=data[row][cell])
        dom.alignment = Alignment(horizontal="center", vertical="center")
        dom.border = Border(left=bd, top=bd, right=bd, bottom=bd)
        if row == 0:
            dom.fill = PatternFill("solid", fgColor="FF6600")
            dom.font = Font(name="Arial", size=12, color="FFFFFF", bold=True)

wb.save("D:/temp/student.xlsx")

```
