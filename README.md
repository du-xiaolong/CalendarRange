# CalendarRange
日历区间选择弹窗
可以选择一个日期区间，支持跨月选择。

截图：

<img src="https://github.com/du-xiaolong/CalendarRange/blob/master/20231010_172021.png" width="400px">
<img src="https://github.com/du-xiaolong/CalendarRange/blob/master/20231010_172014.png" width="400px">


添加jitpack
```
maven { url 'https://jitpack.io' }
```

```
implementation 'com.github.du-xiaolong:CalendarRange:1.0.3'
```


使用方法：
```kotlin
CalendarRangeDialog.show(activity) { start, end ->
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    Log.d(TAG, "开始：${simpleDateFormat.format(start)}，结束：${simpleDateFormat.format(end)}")
}
```
