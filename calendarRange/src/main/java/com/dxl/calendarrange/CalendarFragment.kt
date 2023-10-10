package com.dxl.calendarrange

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Range
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dxl.calendarrange.databinding.FragmentCalendarPagerBinding
import java.util.Calendar

/**
 * 日历控件翻页
 * @author duxiaolong
 * @date 2023/10/9
 */
class CalendarFragment : Fragment() {

    private var year = 0
    private var month = 0

    private val calendarViewModel by viewModels<CalendarViewModel>(this::requireParentFragment)

    private lateinit var calendarPagerBinding: FragmentCalendarPagerBinding
    private val calendarDayAdapter by lazy {
        CalendarDayAdapter().apply {
            onItemClickListener = ::selectDay
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            year = bundle.getInt("year", Calendar.getInstance()[Calendar.YEAR])
            month = bundle.getInt("month", Calendar.getInstance()[Calendar.MONTH] + 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCalendarPagerBinding.inflate(inflater, container, false)
        .also { calendarPagerBinding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
        initData()
    }

    private fun observe() {
        //选择的日期发生变化，更新UI
        calendarViewModel.dataRangeLiveData.observe(viewLifecycleOwner) {
            val days = calendarDayAdapter.days
            for (i in days.indices) {
                val calendarDay = days[i]
                //上个月的不用处理
                if (calendarDay.status == CalendarStatus.LAST_MONTH) continue
                val newStatus = getDayStatus(calendarDay)
                //状态不一致了，更新
                if (newStatus !== calendarDay.status) {
                    calendarDay.status = newStatus
                    calendarDayAdapter.notifyItemChanged(i)
                }
            }
        }
    }

    private fun initView() {
        calendarPagerBinding.tvCurrent.text = year.toString() + "年" + month + "月"
        calendarPagerBinding.rvDays.adapter = calendarDayAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData() {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DAY_OF_MONTH] = 1
        //这个月多少天
        val daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        //1号是星期几,返回1代表 周日，2代表周一
        val firstDayWeek = calendar[Calendar.DAY_OF_WEEK]
        val days: MutableList<CalendarDay> = ArrayList()
        //前面需要补几天上月的，显示为灰色
        val blankDays = (firstDayWeek + 5) % 7
        val lastMonthCalendar = Calendar.getInstance()
        lastMonthCalendar.time = calendar.time
        lastMonthCalendar.add(Calendar.MONTH, -1)
        //上个月一共多少天
        val lastMonthDaysOfMonth = lastMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in lastMonthDaysOfMonth - blankDays + 1..lastMonthDaysOfMonth) {
            val dayCalendar = Calendar.getInstance()
            dayCalendar.time = lastMonthCalendar.time
            dayCalendar[Calendar.DAY_OF_MONTH] = i
            days.add(CalendarDay(dayCalendar, CalendarStatus.LAST_MONTH))
        }

        //本月的
        for (i in 1..daysOfMonth) {
            val dayCalendar = Calendar.getInstance()
            dayCalendar.time = calendar.time
            dayCalendar[Calendar.DAY_OF_MONTH] = i
            val calendarDay = CalendarDay(dayCalendar)
            //默认先设置状态为正常
            calendarDay.status = CalendarStatus.NORMAL
            days.add(calendarDay)
        }
        calendarDayAdapter.setList(days)
    }

    /**
     * 选中了日期
     * 如果已经选择了区间（开始和结束不相同），那么取消原来的选择，重新开始选择
     * 如果原来没有选择任何日期，开始选择
     * 如果原来选择了一个日期，那么用这个日期生成新的区间
     * @param calendarDay
     */
    private fun selectDay(calendarDay: CalendarDay) {
        val dayRange = calendarViewModel.dataRangeLiveData.value
        if (dayRange == null || dayRange.lower != dayRange.upper) {
            calendarViewModel.dataRangeLiveData.value = Range(calendarDay, calendarDay)
        } else {
            var lower = dayRange.lower
            var upper = dayRange.upper
            if (calendarDay < lower) {
                lower = calendarDay
            } else if (calendarDay > upper) {
                upper = calendarDay
            } else if (calendarDay > lower && calendarDay < upper) {
                lower = calendarDay
            }
            calendarViewModel.dataRangeLiveData.value = Range(lower, upper)
        }
    }

    /**
     * 根据日期和已经选择的区间
     * @param calendarDay
     * @return
     */
    private fun getDayStatus(calendarDay: CalendarDay): CalendarStatus {
        val dayRange = calendarViewModel.dataRangeLiveData.value ?: return CalendarStatus.NORMAL
        val lower = dayRange.lower
        val upper = dayRange.upper
        val isSingle = lower.compareTo(upper) == 0
        if (calendarDay.compareTo(lower) == 0 && isSingle) {
            return CalendarStatus.SINGLE_SELECT
        }
        if (calendarDay.compareTo(lower) == 0) {
            return CalendarStatus.START_IN_RANGE
        }
        if (calendarDay.compareTo(upper) == 0) {
            return CalendarStatus.END_IN_RANGE
        }
        return if (dayRange.contains(calendarDay)) {
            CalendarStatus.IN_RANGE
        } else CalendarStatus.NORMAL
    }

    companion object {
        fun newInstance(year: Int, month: Int): CalendarFragment {
            val bundle = Bundle()
            bundle.putInt("year", year)
            bundle.putInt("month", month)
            val calendarFragment = CalendarFragment()
            calendarFragment.arguments = bundle
            return calendarFragment
        }
    }
}