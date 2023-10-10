package com.dxl.calendarrange

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dxl.calendarrange.databinding.ItemDayBinding

/**
 * @author duxiaolong
 * @date 2023/10/9
 */
class CalendarDayAdapter : RecyclerView.Adapter<CalendarDayAdapter.DaysViewHolder>() {

    val days = mutableListOf<CalendarDay>()

    fun setList(calendarDays:List<CalendarDay>) {
        days.clear()
        days.addAll(calendarDays)
        notifyDataSetChanged()
    }

    var onItemClickListener: ((CalendarDay) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        return DaysViewHolder(
            ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        holder.bindDay(days[position])
        holder.itemDayBinding.root.setOnClickListener {
            onItemClickListener?.invoke(days[holder.adapterPosition])
        }
    }

    override fun getItemCount() = days.size

    class DaysViewHolder(val itemDayBinding: ItemDayBinding) :
        RecyclerView.ViewHolder(itemDayBinding.root) {


        fun bindDay(day: CalendarDay) {
            itemDayBinding.vCurrent.isVisible = day.isToday()

            val dayText = itemDayBinding.tvDay
            dayText.setTextColor(
                if (day.isToday())
                    Color.parseColor("#FF4400")
                else
                    Color.parseColor("#212121")
            )
            when (day.status) {
                CalendarStatus.LAST_MONTH -> {
                    dayText.setBackgroundResource(0)
                    dayText.setTextColor(Color.parseColor("#BEC6D1"))
                }

                CalendarStatus.NORMAL -> {
                    dayText.setBackgroundResource(0)
                    dayText.setTextColor(Color.parseColor("#212121"))
                }

                CalendarStatus.START_IN_RANGE -> {
                    dayText.setBackgroundResource(R.drawable.bg_day_start)
                    dayText.setTextColor(Color.WHITE)
                }

                CalendarStatus.END_IN_RANGE -> {
                    dayText.setBackgroundResource(R.drawable.bg_day_end)
                    dayText.setTextColor(Color.WHITE)
                }

                CalendarStatus.IN_RANGE -> {
                    when (day.getWeekDay()) {
                        1 -> {
                            dayText.setBackgroundResource(R.drawable.bg_in_range_start)
                        }

                        7 -> {
                            dayText.setBackgroundResource(R.drawable.bg_in_range_end)
                        }

                        else -> {
                            dayText.setBackgroundResource(R.drawable.bg_in_range)
                        }
                    }
                    dayText.setTextColor(Color.parseColor("#FF4400"))
                }

                CalendarStatus.SINGLE_SELECT -> {
                    dayText.setBackgroundResource(R.drawable.bg_day_single_select)
                    dayText.setTextColor(Color.WHITE)
                }
            }
            dayText.text = day.day.toString() + ""
        }
    }
}