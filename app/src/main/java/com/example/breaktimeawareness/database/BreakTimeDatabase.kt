package com.example.breaktimeawareness.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [BreakTime::class], version = 1,  exportSchema = false)
abstract class BreakTimeDatabase :RoomDatabase(){
    abstract val breakTimeDatabaseDao: BreakTimeDatabaseDao
    companion object {

        @Volatile
        private var INSTANCE: BreakTimeDatabase? = null

        fun getInstance(context: Context): BreakTimeDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BreakTimeDatabase::class.java,
                        "break_time_history_database "
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}