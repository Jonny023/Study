# mui上拉下拉状态关闭

```javascript
// 关闭刷新加载状态
mui('#refreshContainer').pullRefresh().endPulldownToRefresh();
if (len == 0) { // 结束下拉
    mui('#refreshContainer').pullRefresh().endPullupToRefresh(true);
} else {
  // document.getElementById("data-body").innerHTML = (arr.join(''));
  $("#data-body").append(arr.join(''));
  mui('#refreshContainer').pullRefresh().enablePullupToRefresh();
  mui('#refreshContainer').pullRefresh().endPullupToRefresh(false);
  if (len >= page) {
      page++;
  }
}
```
