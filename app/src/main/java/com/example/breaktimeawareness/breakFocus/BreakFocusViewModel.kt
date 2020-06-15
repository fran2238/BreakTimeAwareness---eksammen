package com.example.breaktimeawareness.breakFocus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.breaktimeawareness.database.BreakTimeDatabase
import com.example.breaktimeawareness.database.BreakTimeDatabaseDao
import kotlinx.coroutines.*

class BreakFocusViewModel(
private val breakTimeKey: Long = 0L,
val database: BreakTimeDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        // navigate tilbage til overview livedata
    private val _navigateToOverview = MutableLiveData<Boolean?>()
    val navigateToOverview: LiveData<Boolean?>
        get() = _navigateToOverview


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneNavigating() {
        _navigateToOverview.value = null
    }

    fun onSetBreakFocusLevel(quality: Int) {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val latestBreak = database.get(breakTimeKey) ?: return@withContext
               latestBreak.focusLevel = quality
                database.update(latestBreak)
            }

            // Setting this state variable to true will alert the observer and trigger navigation.
            _navigateToOverview.value = true
        }
    }
}