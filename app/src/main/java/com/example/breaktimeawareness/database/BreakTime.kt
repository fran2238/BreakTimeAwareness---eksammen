package com.example.breaktimeawareness.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "break_time_table")
data class BreakTime(
    @PrimaryKey(autoGenerate = true)
    var breakTimeId: Long = 0L,
    @ColumnInfo(name = "Temperature Level")
    var temperatureLevel: Int = -1,
    @ColumnInfo(name = "Noise Level")
    var noiseLevel: Int = -1,
    @ColumnInfo(name = "Light Level")
    var lightLevel: Int = -1,
    @ColumnInfo(name = "Hours Slept")
    var sleepHours: Int =-1,
    @ColumnInfo(name = "Work Begin")
    var timeStart: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "Work End")
    var timeEnd : Long = timeStart,
    @ColumnInfo(name = "focus Level")
    var focusLevel: Int = -1
) {

}