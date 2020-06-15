package com.example.breaktimeawareness

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.breaktimeawareness.database.BreakTime
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// convert time
private val ONE_MINUTE_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOUR_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

// convert perioder
fun convertDurationToFormatted(startTimeMilli: Long, endTimeMilli: Long, res: Resources): String {
    val durationMilli = endTimeMilli - startTimeMilli
    val weekdayString = SimpleDateFormat("EEEE", Locale.getDefault()).format(startTimeMilli)
    return when {
        durationMilli < ONE_MINUTE_MILLIS -> {
            val seconds = TimeUnit.SECONDS.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.seconds_length, seconds, weekdayString)
        }
        durationMilli < ONE_HOUR_MILLIS -> {
            val minutes = TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.minutes_length, minutes, weekdayString)
        }
        else -> {
            val hours = TimeUnit.HOURS.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.hours_length, hours, weekdayString)
        }
    }
}
// Date formatter
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}
// format breaks
fun formatBreaks(breaks: List<BreakTime>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.title))
        breaks.forEach {
            append("<br>")
            append(resources.getString(R.string.start_time))
            append("\t${convertLongToDateString(it.timeStart)}<br>")
            if (it.timeEnd != it.timeStart) {
                append(resources.getString(R.string.end_time))
                append("\t${convertLongToDateString(it.timeEnd)}<br>")
                append(resources.getString(R.string.quality))
             //   append("\t${convertNumericQualityToString(it.sleepQuality, resources)}<br>")
                append(resources.getString(R.string.hours_slept))
                // Hours
                append("\t ${it.timeEnd.minus(it.timeStart) / 1000 / 60 / 60}:")
                // Minutes
                append("${it.timeEnd.minus(it.timeStart) / 1000 / 60}:")
                // Seconds
                append("${it.timeEnd.minus(it.timeStart) / 1000}<br><br>")
            }
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
fun convertNumericQualityToString(quality: Int, resources: Resources): String {
    var qualityString = resources.getString(R.string.three_quality)
    when (quality) {
        -1 -> qualityString = "--"
        0 -> qualityString = resources.getString(R.string.zero_quality)
        1 -> qualityString = resources.getString(R.string.one_quality)
        2 -> qualityString = resources.getString(R.string.two_quality)
        4 -> qualityString = resources.getString(R.string.four_quality)
        5 -> qualityString = resources.getString(R.string.five_quality)
    }
    return qualityString
}
fun convertPropToString(prop:Int): String{
    var res = "0"
    res = prop.toString()
    return res
}
fun convertColor(prop: Int, resource:Resources):Int{
    var res = resource.getColor(R.color.green_color)


    if (prop > 50){
        res = resource.getColor(R.color.pineapple_yellow)
    }
    if (prop > 75){
        res = resource.getColor(R.color.ruby_red)
    }

    return res
}
