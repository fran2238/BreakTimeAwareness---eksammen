package com.example.breaktimeawareness.breakOverview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breaktimeawareness.database.BreakTimeDatabase
import com.example.breaktimeawareness.database.BreakTimeDatabaseDao
import java.lang.IllegalArgumentException
import javax.sql.DataSource

class BreakTimeViewModelFactory(
    private val dataSource: BreakTimeDatabaseDao,
    private val  application: Application) : ViewModelProvider.Factory{

override fun<T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(BreakOverviewViewModel::class.java)) {
        return BreakOverviewViewModel(dataSource, application) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")

}
}
