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
# coding=utf-8
# cv2解决绘制中文乱码

# import cv2
import numpy as np
from PIL import Image, ImageDraw, ImageFont
from paddle.dataset.image import cv2
from paddleocr import PaddleOCR

font = cv2.FONT_HERSHEY_SIMPLEX

# Paddleocr目前支持中英文、英文、法语、德语、韩语、日语，可以通过修改lang参数进行切换
# 参数依次为`ch`, `en`, `french`, `german`, `korean`, `japan`。
ocr = PaddleOCR(use_angle_cls=True, lang="ch", use_gpu=False, rec_model_dir='../ext/ch_ppocr_server_v2.0_rec_train/',
                cls_model_dir='../ext/ch_ppocr_mobile_v2.0_cls_train/',
                det_model_dir='../ext/ch_ppocr_server_v2.0_det_train/')  # need to run only once to download and load model into memory


def zh_ch(string):
    return string.encode("gbk").decode(errors="ignore")


def putText_Chinese(img, strText, pos, color, fontSize):
    fontpath = "../ext/simsun.ttc"  # <== 这里是宋体路径
    font = ImageFont.truetype(fontpath, fontSize, encoding="utf-8")
    img_pil = Image.fromarray(img)
    draw = ImageDraw.Draw(img_pil)
    draw.text(pos, strText, font=font, fill=color)
    img = np.array(img_pil)
    return img


print('---------------PaddleOCR Start---------------------')
img_path = 'e:/temp/test.png'
img = cv2.imread(img_path)
cv2.imshow(zh_ch("原图"), img)
result = ocr.ocr(img_path, cls=True)  #
print(result)
for line in result:
    print('----------------------------')
    print(line[1][1])
    pt1 = ((tuple)(line[0][0][2]), (str)(line[1][1][0]))
    pt2 = ((tuple)(line[1][0][1]), (str)(line[2][1][0]))
    pt3 = ((tuple)(line[2][0][1]), (str)(line[3][1][0]))
    pt4 = ((tuple)(line[3][0][1]), (str)(line[4][1][0]))
    pt5 = ((tuple)(line[4][0][1]), (str)(line[5][1][0]))
    pt6 = ((tuple)(line[5][0][1]), (str)(line[6][1][0]))
    # pt1 = ((str)(line[0][1][0]), (str)(line[1][1][0]))
    # pt2 = ((str)(line[0][1][0]), (str)(line[0][1][1]))
    # pt3 = ((str)(line[0][2][0]), (str)(line[0][2][1]))
    # pt4 = ((str)(line[0][3][0]), (str)(line[0][3][1]))
    # cv2.line(img, (100, 200), (200, 400), (0, 0, 255), 1, cv2.LINE_AA)
    cv2.rectangle(img, (400, 50), (440, 75), (0, 0, 255), 1, cv2.LINE_AA)
    # cv2.line(img, pt2, pt3, (0, 0, 255), 1, cv2.LINE_AA)
    # cv2.line(img, pt3, pt4, (0, 0, 255), 1, cv2.LINE_AA)
    # cv2.line(img, pt1, pt4, (0, 0, 255), 1, cv2.LINE_AA)
    # img = putText_Chinese(img, line[1][0], (pt1[0], pt1[1] - 35), (255, 0, 255), 50)
    img = putText_Chinese(img, pt1[1], (pt1[0][0] + 208, pt1[0][1] - 35), (255, 0, 255), 25)
    cv2.imshow(zh_ch("ocr识别结果"), img)
    cv2.imwrite("result.png", img)
    cv2.waitKey()
    cv2.destroyAllWindows()
# -----------------------------------
# 官网地址：https://pypi.org/project/paddleocr/
# 基于OpenCV实现文字识别步骤与代码展示https://blog.51cto.com/u_14411234/3115360
# https://blog.51cto.com/u_14411234/3115360

```
