package com.dxl.calendarrange

import java.util.Calendar

/**
 *
 * @author duxiaolong
 * @date 2023/10/10
 */
data class CalendarDay(
    val year: Int,
    val month: Int,
    val day: Int,
    var status: CalendarStatus = CalendarStatus.NORMAL
) : Comparable<CalendarDay> {

    constructor(calendar: Calendar, status: CalendarStatus = CalendarStatus.NORMAL) : this(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH] + 1,
        calendar[Calendar.DAY_OF_MONTH],
        status
    )

    override fun compareTo(other: CalendarDay): Int {
        if (year < other.year) return -1
        if (year > other.year) return 1
        if (month < other.month) return -1
        if (month > other.month) return 1
        if (day < other.day) return -1
        return if (day > other.day) 1 else 0
    }

    val calendar: Calendar
        get() = Calendar.getInstance().apply {
            this[Calendar.YEAR] = year
            this[Calendar.MONTH] = month - 1
            this[Calendar.DAY_OF_MONTH] = day
        }

    fun getWeekDay(): Int {
        var i = (calendar[Calendar.DAY_OF_WEEK] + 6) % 7
        if (i == 0) i = 7
        return i
    }

    fun isToday(): Boolean {
        val todayCalendar = Calendar.getInstance()
        return todayCalendar[Calendar.YEAR] == year && todayCalendar[Calendar.MONTH] + 1 == month && todayCalendar[Calendar.DAY_OF_MONTH] == day
    }

}
