## Python django

[参考1](https://docs.djangoproject.com/zh-hans/4.0/intro/tutorial01/)

[参考2](https://www.django.cn/forum/forum-5.html)

```sh
# 创建项目
django-admin startproject myapp

# 新建app，有点类似module
python manage.py startapp blog

# 目录结构
myapp/
  manage.py
  myapp/
    __init__.py
    settings.py
    urls.py
    asgi.py
    wsgi.py

# 启动提示：no module named myapp
# 在manage.py中配置项目路径
import sys
sys.path.append(r'../myapp')

# 运行
python manage.py runserver 8080

# 默认端口8000
python manage.py runserver
```

