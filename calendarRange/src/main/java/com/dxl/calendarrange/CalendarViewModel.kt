package com.dxl.calendarrange

import android.util.Range
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author duxiaolong
 * @date 2023/10/9
 */
class CalendarViewModel : ViewModel() {

    var dataRangeLiveData = MutableLiveData<Range<CalendarDay>>()
}