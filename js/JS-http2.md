# 验证网站是否使用了HTTP2

```javascript
(function(){
  //验证网站是否使用了http2
  if(window.chrome && typeof chrome.loadTimes === 'function') {
    var loadTimes = window.chrome.loadTimes()
    var spdy = loadTimes.wasFetchedViaSpdy
    var info = loadTimes.npnNegotiatedProtocol || loadTimes.connectionInfo
    if(spdy && /^h2/i.test(info)) {
      return console.log('本站点使用了 http2')
    }
  }
  console.warn('本站点未使用 http2')
})()

```