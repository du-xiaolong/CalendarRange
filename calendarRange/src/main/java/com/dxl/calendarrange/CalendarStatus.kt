package com.dxl.calendarrange

/**
 *
 * @author duxiaolong
 * @date 2023/10/10
 */
enum class CalendarStatus {
    /**
     * 上个月的（灰色）
     */
    LAST_MONTH,
    /**
     * 正常状态，未选中
     */
    NORMAL,
    /**
     * 区间开始
     */
    START_IN_RANGE,
    /**
     * 区间结束
     */
    END_IN_RANGE,
    /**
     * 区间中间的部分
     */
    IN_RANGE,
    /**
     * 单选（之选中了一个）
     */
    SINGLE_SELECT
}