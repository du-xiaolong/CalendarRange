package com.dxl.calendarrange

import java.util.Date

/**
 * @author duxiaolong
 * @date 2023/10/9
 */
interface DateRangeSelectListener {
    fun onDateRangeSelect(start: Date, end: Date)
}