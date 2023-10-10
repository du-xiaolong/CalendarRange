# CalendarDemo
简单的日历区间选择弹窗



使用方法：
```
CalendarRangeDialog.show(activity) { start, end ->
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    Log.d(TAG, "开始：${simpleDateFormat.format(start)}，结束：${simpleDateFormat.format(end)}")
}
```
