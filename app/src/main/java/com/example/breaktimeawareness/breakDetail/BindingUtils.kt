package com.example.breaktimeawareness.breakDetail

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.breaktimeawareness.*
import com.example.breaktimeawareness.R.*
import com.example.breaktimeawareness.database.BreakTime

@BindingAdapter("lightLevelString")
fun TextView.setLightLevelString(item: BreakTime?){
    item?.let {
        if (item.lightLevel >= 50){
            setTextColor(Color.parseColor("#FEE227"))
        }
        if (item.lightLevel >= 75){
            setTextColor(Color.parseColor("#FF2400"))
        }
        text = convertPropToString(item.lightLevel)
    }

}

@BindingAdapter("noiseLevelString")
fun TextView.setNoiseLevelString(item: BreakTime?) {
    item?.let {
        if (item.noiseLevel >= 50){
            setTextColor(Color.parseColor("#FEE227"))
        }
        if (item.noiseLevel >= 75){
           setTextColor(Color.parseColor("#FF2400"))
        }


        text = "Støj Niveauet var : " + convertPropToString(item.noiseLevel)
    }

}
@BindingAdapter("sleepLevelString")
fun TextView.setSleepLevelString(item: BreakTime?) {
    item?.let {

        if (item.sleepHours >= 4){
            setTextColor(Color.parseColor("#FF2400"))
        }
        if (item.sleepHours <= 6 || item.sleepHours >= 10){
            setTextColor(Color.parseColor("#FEE227"))
        }
        text = "Havde Sovet " + convertPropToString(item.sleepHours) + " Timer"
    }

}
@BindingAdapter("temperatureLevelString")
fun TextView.setTemperatureLevelString(item: BreakTime?) {
    item?.let {
        if (item.temperatureLevel < 0){
            setTextColor(Color.parseColor("#0B1171"))
        }
        if (item.temperatureLevel >= 21){
            setTextColor(Color.parseColor("#FF2400"))
        }
        text = "Temperaturen var : " + convertPropToString(item.temperatureLevel) + "° grader celsius"
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
        text =  convertNumericQualityToString(item.focusLevel, context.resources)
    }

}


