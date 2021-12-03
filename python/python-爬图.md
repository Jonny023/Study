## 安装工具包

```shell
pip install requests
pip install lxml
```

## 爬取图片

```shell
import os.path

import requests
from lxml import etree
import urllib

path = 'd:\\img\\'
url = 'http://www.daimg.com/photo/'
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
                  "Chrome/87.0.4280.88 Safari/537.36"
}
rep = requests.get(url, headers=headers)
# print(rep.text)
# rep.encoding = 'utf-8'
rep.encoding = 'gbk'

html_data = etree.HTML(rep.text)
img_url_list = html_data.xpath("/html/body/div[5]/ul/li[*]/a/img")

for img_tag in img_url_list:
    title = img_tag.attrib['title']
    src = img_tag.attrib['src']
    file_name = os.path.basename(src)
    urllib.request.urlretrieve(src, '{0}{1}'.format(path, file_name))
```

