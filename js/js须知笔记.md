## 监听事件

- 使用`monitorEvents()`监听某一类型的事件。
- 使用`unmonitorEvents()`停止监听。
- 使用`getEventListeners()`获取DOM元素的监听器。
- 使用`Event Listeners Inspector`(事件监听检查器)面板获取有关事件监听器的信息。

> ### `monitorEvents()`监听某一事件

```javascript
// 监听点击事件
monitorEvents(document.body, "click");

// 停止监听
unmonitorEvents(document.body);

monitorEvents(window, ["resize", "scroll"])
```

### 事件列表

| 时间类型 | 对应的映射事件                                               |
| -------- | ------------------------------------------------------------ |
| mouse    | "mousedown", "mouseup", "click", "dblclick", "mousemove", "mouseover", "mouseout", "mousewheel" |
| key      | "keydown", "keyup", "keypress", "textInput"                  |
| touch    | "touchstart", "touchmove", "touchend", "touchcancel"         |
| control  | "resize", "scroll", "zoom", "focus", "blur", "select", "change", "submit", "reset" |

> ### `getEventListeners()`查看绑定在某个对象上的事件监听器

```javascript
getEventListeners(document);
getEventListeners(document.body);
```

> ### `keys`

* 获取对象所有的`key`值，返回一个数组

```javascript
keys({id:2,age:18,sex:'男'})
// 返回 ["id","age","sex"]
```



> ### `values`

* 获取对象所有的`value`值，返回一个数组

```javascript
values({id:2,age:18,sex:'男'})
// 返回 [2, 18, "男"]
```

> ### `charCodeAt()`

* 获取字符对应的`ascii`码

* 范围：

  | 字符范围 | 值范围 |
  | -------- | ------ |
  | a-z      | 97-122 |
  | A-Z      | 65-90  |
  | 0-9      | 48-57  |

```javascript
'a'.charCodeAt(); // 97
```

> ### `String.fromCharCode()`

* 将对应的`ascii`码值转为对应的字符

```javascript
String.fromCharCode(122); // 'z'
```

