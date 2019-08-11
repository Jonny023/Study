# Python邮件服务

### 安装依赖

```bash
pip install pyEmail
```

### 项目使用

```python
import smtplib
```

* 代码

```python
#!/usr/bin/python3

# 参考地址：https://www.runoob.com/python3/python3-smtp.html

import smtplib
from email.mime.text import MIMEText
from email.header import Header

# 第三方 SMTP 服务
mail_host = "smtp.qq.com"  # 设置服务器
mail_user = "2268999743@qq.com"  # 用户名
mail_pass = "nwyfoaojxketecid"  # 口令

sender = '2268999743@qq.com'
receivers = ['342418262@qq.com']  # 接收邮件，可设置为你的QQ邮箱或者其他邮箱

message = MIMEText('Python 邮件发送测试...', 'text', 'utf-8')
message['From'] = Header("菜鸟教程", 'utf-8')
message['To'] = Header("测试", 'utf-8')

subject = 'Python SMTP 邮件测试'
message['Subject'] = Header(subject, 'utf-8')

try:
    # smtp = smtplib.SMTP()
    # smtp.connect(mail_host, 465)  # 25 为 SMTP 端口号

    # QQ邮件服务器用SMTP_SSL，465/587端口需要SSL
    smtp = smtplib.SMTP_SSL(mail_host, 465)
    smtp.login(mail_user, mail_pass)
    smtp.sendmail(sender, receivers, message.as_string())
    print("邮件发送成功")
    smtp.quit()
except smtplib.SMTPException as e:
    print("Error: 无法发送邮件")
    print(e)
```
