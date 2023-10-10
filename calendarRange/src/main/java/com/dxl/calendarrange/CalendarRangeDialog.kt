package com.dxl.calendarrange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dxl.calendarrange.databinding.DialogCalendarRangeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar
import java.util.Date

/**
 * 选择日期区间
 *
 * @author duxiaolong
 * @date 2023/10/9
 */
class CalendarRangeDialog : BottomSheetDialogFragment() {
    /**
     * 当前fragment和子fragment共享一份viewModel，便于数据传递
     */
    private val calendarViewModel by viewModels<CalendarViewModel>()

    /**
     * 确定日期区间回调
     */
    private var onDateSelect: ((start: Date, end: Date) -> Unit)? = null

    /**
     * ViewBinding数据绑定
     */
    private lateinit var binding: DialogCalendarRangeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        DialogCalendarRangeBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        //日期区间默认为当前时间前后两年
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.YEAR, -2)

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.YEAR, 2)
        val months: MutableList<Calendar> = ArrayList()
        var defaultIndex = -1

        //生成月份列表
        for (i in 0 until Int.MAX_VALUE) {
            val calendar = Calendar.getInstance()
            calendar.time = startCalendar.time
            calendar.add(Calendar.MONTH, i)
            if (calendar <= endCalendar) {
                months.add(calendar)
                if (calendar[Calendar.YEAR] == Calendar.getInstance()[Calendar.YEAR] && calendar[Calendar.MONTH] == Calendar.getInstance()[Calendar.MONTH]) {
                    //记录当前月份的索引
                    defaultIndex = i
                }
            } else {
                break
            }
        }
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                val calendar = months[position]
                return CalendarFragment.newInstance(
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH] + 1
                )
            }

            override fun getItemCount() = months.size
        }
        //默认选中当前月份
        binding.viewPager.setCurrentItem(defaultIndex, false)
        //确认
        binding.btnConfirm.setOnClickListener {
            val dayRange = calendarViewModel.dataRangeLiveData.value
            if (dayRange == null) {
                Toast.makeText(requireContext(), "请选择日期", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            onDateSelect?.invoke(
                dayRange.lower.calendar.time,
                dayRange.upper.calendar.time
            )
            dismissAllowingStateLoss()
        }
        //关闭
        binding.ivClose.setOnClickListener { dismissAllowingStateLoss() }
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(binding.root.parent as View)
        behavior.skipCollapsed = true
        behavior.skipCollapsed = true
    }

    companion object {
        /**
         * activity中调用
         * @param activity
         * @param onDateSelect 选中回调
         */
        fun show(activity: FragmentActivity, onDateSelect: (start: Date, end: Date) -> Unit) {
            val calendarRangeDialog = CalendarRangeDialog()
            calendarRangeDialog.onDateSelect = onDateSelect
            calendarRangeDialog.show(activity.supportFragmentManager, "")
        }

        /**
         * fragment中调用
         * @param fragment
         * @param onDateSelect 选中回调
         */
        fun show(fragment: Fragment, onDateSelect: (start: Date, end: Date) -> Unit) {
            val calendarRangeDialog = CalendarRangeDialog()
            calendarRangeDialog.onDateSelect = onDateSelect
            calendarRangeDialog.show(fragment.childFragmentManager, "")
        }
    }
}