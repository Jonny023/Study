# 页面图片路径不存在或没有src属性

```html
<style>
    img[src=""],img:not([src]) {
        opacity: 0;
    }
</style>
```
