# CalendarDemo
简单的日历区间选择弹窗

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
