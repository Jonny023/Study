# 获取指定周

```html
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>日历操作</title>
    <script>
        window.onload = function () {
            var dateArr = [];
            var currentFirstDate;
            var formatDate = function (date) {
                var year = date.getFullYear() + '-';
                var month = (date.getMonth() + 1) + '-';
                var day = date.getDate();
                return year + month + day
            };

            var addDate = function (date, n) {
                date.setDate(date.getDate() + n);
                return date;
            };

            var setDate = function (date) {
                var week = date.getDay() - 1;
                console.log(date);
                console.log("today:");
                date = addDate(date, week * -1);
                console.log(date.getDate())
                currentFirstDate = new Date(date);
                console.log(currentFirstDate);
                for (var i = 0; i < 7; i++) {
                    dateArr[i] = formatDate(i == 0 ? date : addDate(date, 1));
                }
            };

            // 上一周
            document.getElementById('last-week').onclick = function () {
                setDate(addDate(currentFirstDate, -7));
                console.log(dateArr);
            };

            // 下一周
            document.getElementById('next-week').onclick = function () {
                setDate(addDate(currentFirstDate, 7));
                console.log(dateArr);
            };

            // 获取本周
            function getDates() {
                var new_Date = new Date()
                var timesStamp = new_Date.getTime();
                var currenDay = new_Date.getDay();
                for (var i = 0; i < 7; i++) {
                    if(i == 0) {
                      currentFirstDate = new Date(timesStamp + 24 * 60 * 60 * 1000 * (i - (currenDay + 6) % 7));
                    }
                   console.log(new Date(timesStamp + 24 * 60 * 60 * 1000 * (i - (currenDay + 6) % 7)));
                   dateArr[i] = (new Date(timesStamp + 24 * 60 * 60 * 1000 * (i - (currenDay + 6) % 7)).toLocaleDateString().replace(/[年月]/g, '-').replace(/[日上下午]/g, '').replace(/\//g, '-'));
                }
            }

            // 本周
            document.getElementById('current-week').onclick = function () {
                getDates();
                console.log(dateArr);
            };

            getDates();
        }
    </script>
</head>
<body>
<button id="last-week">上一周</button>
<button id="next-week">下一周</button>
<button id="current-week">本周</button>
</body>
</html>

```
