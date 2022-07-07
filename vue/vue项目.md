## vue

```sh
1.创建目录，用vscode打开

2.npm install -y

# vue，默认为vue3,若需要vue2需要执行：npm i vue@2
3.npm install vue
```

## 页面

#### checkbox绑定

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <div id="app">
        <input type="checkbox" v-model="language" value="Java">Java<br />
        <input type="checkbox" v-model="language" value="Groovy">Groovy<br />
        <input type="checkbox" v-model="language" value="Php">Php<br />
        <span>选中：{{language.join(",")}}</span>
    </div>

    <script src="./node_modules/vue/dist/vue.js"></script>
    <script>
        let vm = new Vue({
            el: '#app',
            data: {
                language: []
            }
        });
    </script>
</body>

</html>
```

#### 事件

```html
	<div id="app">
        <input type="text" v-model="num" />
        <button v-on:click="num++">点赞</button>
        <button v-on:click="cancel">取消</button>
        <h2>你好，我是{{name}} 当前点赞数{{num}}</h2>
    </div>

    <script src="./node_modules/vue/dist/vue.js"></script>

    <script>
        let vm = new Vue({
            el: "#app",
            data: {
                name: "李四",
                num: 0
            },
            methods: {
                cancel() {
                    this.num--;
                }
            }
        });
    </script>
```

#### v-for

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <div id="app">
        
        <ul>
            <li v-for="user in users">
                {{user.id}} {{user.name}}  {{user.age}}
            </li>
        </ul>

        <ul>
            <li v-for="(user, index) in users">
                索引：{{index}}， {{user.id}} {{user.name}}  {{user.age}}<br/>
                对象信息：<span v-for="(v,k) in user">  {{k}}={{v}} </span><br/>
                对象信息：<span v-for="(v,k,i) in user">  {{i}}={{k}}={{v}} </span>
            </li>
        </ul>
        <ul>
            <li v-for="(num, index) in nums" :key="index">
                {{index}} -- {{num}}
            </li>
        </ul>
    </div>
    <script src="./node_modules/vue/dist/vue.js"></script>
    <script>

        let vm = new Vue({
            el: "#app",
            data: {
                users:[
                    {id: 1, name: "zhangsan", age: 20},
                    {id: 2, name: "lisi", age: 18},
                    {id: 3, name: "wangwu", age: 30},
                    {id: 4, name: "zhaoliu", age: 33},
                    {id: 5, name: "sunbing", age: 21},
                    {id: 6, name: "zhengpeng", age: 25},
                ],
                nums: [1,2,3,4,5,6]
            }
        });
    </script>
</body>
</html>
```



* v-model 双向绑定

* v-on 绑定事件，可用@代替

  * ```html
    <button v-on:click="num++">点赞</button><br/>
    <button @click="num++">点赞</button><br/>
    ```

* v-html 渲染html

* v-bind 数据绑定(单向绑定)

  * ```html
    <a v-bind:href="link">百度</a>
    
    //可以简写
    <a :href="link">百度</a>
    ```

* v-if 条件判断
* v-show 显示与隐藏

#### 计算属性和侦听器

```html
	<div id="app">
        <ul>
            <li>西瓜：{{xgPrice}}， 数量：<input type="number" v-model="xgNum" /></li>
            <li>哈密瓜：{{hmgPrice}}， 数量：<input type="number" v-model="hmgNum" /></li>
            <li>总价：{{totalPrice}}</li>
            <li><i style="color: red">{{msg}}</i></li>
        </ul>
    </div>
    <script src="./node_modules/vue/dist/vue.js"></script>
    <script>

        let vm = new Vue({
            el: "#app",
            data: {
                xgPrice: 3.2,
                hmgPrice: 5.8,
                xgNum: 1,
                hmgNum: 1,
                msg: ""
            },
            computed: {
                totalPrice() {
                    return this.xgNum * this.xgPrice + this.hmgNum * this.hmgPrice;
                }
            },
            watch: {
                xgNum: function (newVal, oldVal) {
                    if (newVal <= 0) {
                        this.msg = "数量至少为1";
                        this.xgNum = 0;
                    } else {
                        this.msg = "";
                    }
                }
            }
        });
    </script>
```

#### 过滤器

##### 局部过滤器

```html
	<div id="app">
        <ul>
            <li v-for="user in userList">
                {{user.id}}=={{user.name}}=={{user.sex | sexFilter}}
            </li>
        </ul>
    </div>
    <script src="./node_modules/vue/dist/vue.js"></script>
    <script>

        let vm = new Vue({
            el: "#app",
            data: {
                userList: [
                   {id: 1, name: "zhangsan", sex: 0}, 
                   {id: 2, name: "lisi", sex: 1}, 
                   {id: 3, name: "wangwu", sex: 1}, 
                   {id: 4, name: "zhaoliu", sex: 0}, 
                ]
            }, 
            filters: {
                sexFilter(val){
                    if (val === 0) {
                        return '男';
                    } else {
                        return '女';
                    }
                }
            }
        });
    </script>
```

##### 全局过滤器

```html
	<div id="app">
        <ul>
            <li v-for="user in userList">
                {{user.id}}=={{user.name}}=={{user.sex | gFilter}}
            </li>
        </ul>
    </div>
    <script src="./node_modules/vue/dist/vue.js"></script>
    <script>
        Vue.filter("gFilter", function (val) {
            if (val === 0) {
                return '男1';
            } else {
                return '女1';
            }
        });

        let vm = new Vue({
            el: "#app",
            data: {
                userList: [
                    { id: 1, name: "zhangsan", sex: 0 },
                    { id: 2, name: "lisi", sex: 1 },
                    { id: 3, name: "wangwu", sex: 1 },
                    { id: 4, name: "zhaoliu", sex: 0 },
                ]
            }
        });
    </script>
```

#### 组件

##### 全局组件

```html
	<div id="app">
        <counter></counter>
        <counter></counter>
    </div>

    <script src="./node_modules/vue/dist/vue.js"></script>
    <script>
        Vue.component("counter", {
            template: `<button v-on:click="counter++">点击了{{counter}}次</button>`,
            data() {
                return {
                    counter: 1
                }
            }
        });
        let vm = new Vue({
            el: "#app",
            data: {
                num: 0
            }
        });
    </script>
```

##### 局部组件

```html
	<div id="app">
        <button-counter></button-counter>
        <button-counter></button-counter>
    </div>

    <script src="./node_modules/vue/dist/vue.js"></script>
    <script>
        //局部组件
        const buttonCounter = {
            template: `<button v-on:click="counter++">点击了{{counter}}次</button>`,
            data() {
                return {
                    counter: 1
                }
            }
        };
        let vm = new Vue({
            el: "#app",
            data: {
                num: 0
            }, 
            components: {
                "button-counter": buttonCounter
            }
        });
    </script>
```

#### 生命周期

[生命周期](https://cn.vuejs.org/v2/guide/instance.html#%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F%E5%9B%BE%E7%A4%BA)



#### vue-cli

> vue脚手架

##### 安装

```sh
# 全局安装webpack，打包项目
npm install webpack -g

npm install -g vue

# 全局安装脚手架
npm install -g vue-cli
# 卸载
npm uninstall vue-cli -g
# 安装
npm install -g @vue/cli
# 或者 
cnpm install -g @vue/cli
# 或者
yarn global add @vue/cli

# 查看npm
npm config list


# 初始化vue项目，初始化一个appname的vue项目
vue init webpack appname

cd appname

# 启动
npm run dev

# 编译
npm run build
```

##### 路由跳转

```vue
<template>
  <div id="app">
    <img src="./assets/logo.png">
    <router-link to="/hello">Hello</router-link>
    <router-link to="/">首页</router-link>
    <router-view/>
  </div>
</template>
```





#### element-ui

[官网](https://element.eleme.cn/#/zh-CN/component/installation)

```sh
# 安装
npm i element-ui -S
```

> 在main.js中引入

```vue
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI);
```





#### 问题

* **Expected indentation of 4 spaces but found 8.**

```js
//注释掉webpack.base.conf.js文件中的代码，注释部分
module: {
    rules: [
      // ...(config.dev.useEslint ? [createLintingRule()] : []),
    ]
}
```

* **Strings must use singlequote.eslint**

> 在.eslintrc.js文件的rules下配置

```js
    'space-before-function-paren': 0,
    'indent': 'off',
	'semi': 0 //不检查分号
```

* **Newline required at end of file but not found**

> 要在报错行后面加一个回车换行

* eslint严格语法检查

> 注释掉build/webpack.base.conf.js中的createLintingRule方法的规则

```js
//注释掉
const createLintingRule = () => ({
  // test: /\.(js|vue)$/,
  // loader: 'eslint-loader',
  // enforce: 'pre',
  // include: [resolve('src'), resolve('test')],
  // options: {
  //   formatter: require('eslint-friendly-formatter'),
  //   emitWarning: !config.dev.showEslintErrorsInOverlay
  // }
})
```

