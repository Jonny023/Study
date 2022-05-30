## ES6基础

* 修改对象key值

```javascript
var datas = [{id:4,name:2,selected: true},{id:1,name:2,selected: true},{id:2,name:2,selected: false},{id:3,name:2,selected: true}];
datas.map(o=> { return {id:o.id,name:o.name,LAY_CHECKED: o.selected}});
```

* 求和

```javascript
let list = [{value: 20},{value: 10}, {value: 8}];
list.map(elem => elem.value).reduce((x,y) => x + y);
38

```
