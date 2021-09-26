# 下载进度

## 定义工具`axiosDownload.js`

```vue
// Axios拦截请求并实现下载
import axios from 'axios'

// download url
export const downloadUrl = (url) => {
  console.log(url)
  let iframe = document.createElement('iframe')
  iframe.style.display = 'none'
  iframe.src = url
  iframe.onload = function () {
    document.body.removeChild(iframe)
  }
  document.body.appendChild(iframe)
}

// Add a response interceptor
// res返回的东西可以跟后端确认
axios.interceptors.response.use(res => {
  if (res.data.status && res.data.status === 300) {
    return '300' // 链接正确，下载失败
  } else {
    downloadUrl(res.request.responseURL)
    return '200' // 链接正确，下载成功
  }
}, error => {
  // Do something with response error
  return error // 链接错误
})

export default axios
```

## 调用

```vue
import axios from './axiosDownload'
// 导出 或 下载
exportDoc () {
  let URL = `下载地址`
  let me = this
  axios.get(URL).then(function (response) {
    if (response === '200') {
      me.$message.success('下载成功！')
    } else {
      me.$message.warning('下载失败！')
    }
  }).catch(function (response) {
    console.log(response);
  });
}
```

