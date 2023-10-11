# CalendarRange
日历区间选择弹窗
可以选择一个日期区间，支持跨月选择。

截图：
![Screenshot1](https://github.com/du-xiaolong/CalendarRange/blob/master/20231010_172021.png)
![Screenshot1](https://github.com/du-xiaolong/CalendarRange/blob/master/20231010_172014.png)

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
