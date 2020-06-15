package com.example.breaktimeawareness.breakOverview

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.breaktimeawareness.R
import com.example.breaktimeawareness.convertDurationToFormatted
import com.example.breaktimeawareness.convertNumericQualityToString
import com.example.breaktimeawareness.convertPropToString
import com.example.breaktimeawareness.database.BreakTime
@BindingAdapter("breakImage")
fun ImageView.setBreakImage(item: BreakTime?) {
    item?.let {
        setImageResource(when (item.focusLevel) {
            0 -> R.drawable.ic_focus_0
            1 -> R.drawable.ic_focus_1
            2 -> R.drawable.ic_focus_2
            3 -> R.drawable.ic_focus_3
            4 -> R.drawable.ic_focus_4
            5 -> R.drawable.ic_focus_5
            else -> R.drawable.ic_focus_active
        })
    }
}

@BindingAdapter("breakDurationFormatted")
fun TextView.setBreakDurationFormatted(item: BreakTime?) {
    item?.let {
        text = convertDurationToFormatted(item.timeStart, item.timeEnd, context.resources)
    }
}
@BindingAdapter("breakQualityString")
fun TextView.setFocusQualityString(item: BreakTime?) {
    item?.let {
        text = convertNumericQualityToString(item.focusLevel, context.resources)
    }
}
