package com.example.breaktimeawareness.database

import android.os.FileObserver.DELETE
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
@Dao
interface BreakTimeDatabaseDao{

    @Insert
    fun insert(breakTime: BreakTime)

    @Update
    fun update(breakTime: BreakTime)

    @Query("SELECT * FROM break_time_table WHERE breakTimeId = :key")
    fun get(key : Long): BreakTime?

    @Query("DELETE FROM break_time_table" )
    fun clear()

    @Query("SELECT * FROM break_time_table ORDER BY breakTimeId DESC")
    fun getAllBreaks(): LiveData<List<BreakTime>>

    @Query("SELECT * FROM break_time_table ORDER BY breakTimeId DESC LIMIT 1")
    fun getLatestBreak(): BreakTime?

    @Query("SELECT * FROM break_time_table WHERE breakTimeId = :key")
    fun getBreakWithId(key: Long): LiveData<BreakTime>
}