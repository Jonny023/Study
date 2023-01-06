## ocr图片文字提取

### demo1

```python
from paddleocr import PaddleOCR

ocr = PaddleOCR(use_angle_cls=True, )
# 输入待识别图片路径
img_path = r"e:/temp/test.png"
# 输出结果保存路径
result = ocr.ocr(img_path, cls=True)
print(result)
results = []
for line in result:
    for lists in line:
        print(lists[1])
        results.append(lists[1][0])

print(results)
```


### demo2
```python
# coding:utf-8
# cv2解决绘制中文乱码
import time

# import cv2
import numpy as np
from PIL import Image, ImageDraw, ImageFont
from paddle.dataset.image import cv2
from paddleocr import PaddleOCR

font = cv2.FONT_HERSHEY_SIMPLEX

# 官网地址：https://pypi.org/project/paddleocr/
# 基于OpenCV实现文字识别步骤与代码展示： https://blog.51cto.com/u_14411234/3115360
# Paddleocr目前支持中英文、英文、法语、德语、韩语、日语，可以通过修改lang参数进行切换
# 参数依次为`ch`, `en`, `french`, `german`, `korean`, `japan`。
ocr = PaddleOCR(use_angle_cls=True, lang="ch", use_gpu=False, rec_model_dir='../ext/ch_ppocr_server_v2.0_rec_train/',
                cls_model_dir='../ext/ch_ppocr_mobile_v2.0_cls_train/',
                det_model_dir='../ext/ch_ppocr_server_v2.0_det_train/')  # need to run only once to download and load model into memory


def zh_ch(string):
    # return string.encode("gbk").decode('utf-8', errors='ignore')
    return string.encode("gbk").decode(errors='ignore')


def putText_Chinese(img, strText, pos, color, fontSize):
    font_path = "../ext/simsun.ttc"  # <== 这里是宋体路径
    font = ImageFont.truetype(font_path, fontSize, encoding="utf-8")
    img_pil = Image.fromarray(img)
    draw = ImageDraw.Draw(img_pil)
    draw.text(pos, strText, font=font, fill=color)
    img = np.array(img_pil)
    return img


print('---------------PaddleOCR Start---------------------')
start = time.time()
img_path = 'e:/temp/test.png'
img = cv2.imread(img_path)
cv2.imshow(zh_ch("原图"), img)
result = ocr.ocr(img_path, cls=True)
# 坐标：左上、右上、左下、右下 QT5
# 内层循环就是获取原文字坐标和文字内容，cv2.line是画出线条，将原文字的四个边框起来
print(result)
for elem in result:
    for line in elem:
        print("识别内容：", line[1][0])
        pt1 = (int(line[0][0][0]), int(line[0][0][1]))
        pt2 = (int(line[0][1][0]), int(line[0][1][1]))
        pt3 = (int(line[0][2][0]), int(line[0][2][1]))
        pt4 = (int(line[0][3][0]), int(line[0][3][1]))
        cv2.line(img, pt1, pt2, (0, 0, 255), 1, cv2.LINE_AA)
        cv2.line(img, pt2, pt3, (0, 0, 255), 1, cv2.LINE_AA)
        cv2.line(img, pt3, pt4, (0, 0, 255), 1, cv2.LINE_AA)
        cv2.line(img, pt1, pt4, (0, 0, 255), 1, cv2.LINE_AA)
        img = putText_Chinese(img, line[1][0], (pt1[0], pt1[1] - 12), (255, 0, 0), 18)
cv2.imshow(zh_ch('识别结果'), img)
cv2.imwrite("result.png", img)
print(f'coast:{time.time() - start:.8f}s')
cv2.waitKey()
cv2.destroyAllWindows()


```
