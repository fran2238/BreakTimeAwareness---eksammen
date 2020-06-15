package com.example.breaktimeawareness.breakDetail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.breaktimeawareness.database.BreakTime
import com.example.breaktimeawareness.database.BreakTimeDatabase
import com.example.breaktimeawareness.database.BreakTimeDatabaseDao
import kotlinx.coroutines.Job
import androidx.lifecycle.LiveData
import javax.sql.DataSource

class BreakDetailViewModel(
    private val breakTimeKey: Long= 0L,
    dataSource: BreakTimeDatabaseDao) : ViewModel() {

   val database : BreakTimeDatabaseDao = dataSource

    private val viewModelJob: Job = Job()

    private val breakTime = MediatorLiveData<BreakTime>()

    fun getBreakTime() = breakTime

    init {
        breakTime.addSource(database.getBreakWithId(breakTimeKey), breakTime::setValue)
    }

    private val _navigateToOverView = MutableLiveData<Boolean?>()

    val navigateToOverView :LiveData<Boolean?>
        get() =_navigateToOverView

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneNavigating() {
        _navigateToOverView.value =null
    }

    fun onClose() {
        _navigateToOverView.value = true
    }
}