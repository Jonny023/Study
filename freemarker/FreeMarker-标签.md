# FreeMarker标签

* 判断对象非空

```html
<#if user?? && user.price > 200>

</#if>
```

* 判断集合非空

```
<#if list?? && list?size >0>

</#if>
```
