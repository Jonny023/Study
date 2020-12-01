* 编辑vue.config.js

```vue
//打包后绝对路径/gdh5/gdh5_assets/xxx.js
//若要相对路径publicPath: './gdh5'
module.exports={
	publicPath :'/gdh5',
	assetsDir:'gdh5_assets',
	outputDir:"gdh5"
}
```
