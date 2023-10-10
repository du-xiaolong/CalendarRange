package com.dxl.calendardemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.dxl.calendarrange.CalendarRangeDialog
import com.dxl.calendarrange.DateRangeSelectListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn_show).setOnClickListener {
            CalendarRangeDialog.show(this) { start, end ->
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                findViewById<TextView>(R.id.tv_info).text = "开始：${simpleDateFormat.format(start)}，结束：${simpleDateFormat.format(end)}"
            }
        }
    }
}