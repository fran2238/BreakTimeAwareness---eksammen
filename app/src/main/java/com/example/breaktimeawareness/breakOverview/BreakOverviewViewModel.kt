package com.example.breaktimeawareness.breakOverview

import android.app.Application
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.widget.Toast
import androidx.lifecycle.*
import com.example.breaktimeawareness.database.BreakTime
import com.example.breaktimeawareness.database.BreakTimeDatabaseDao
import kotlinx.coroutines.*

private val FIRST_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val SECOND_BUZZ_PATTERN = longArrayOf(0, 200)
private val FINAL_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class BreakOverviewViewModel(
    val database :BreakTimeDatabaseDao,
    application: Application): AndroidViewModel(application) {

    enum class BuzzType(val pattern: LongArray) {
        FIRST(FIRST_BUZZ_PATTERN),
        SECOND(SECOND_BUZZ_PATTERN),
        FINAL(FINAL_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }
    private val _buzzEvent = MutableLiveData<BuzzType>()
    val buzzEvent:LiveData<BuzzType>
        get() = _buzzEvent

    fun onBuzzComplete(){
        _buzzEvent.value = BuzzType.NO_BUZZ
    }
    private var viewModelJob: Job = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var latestBreak = MutableLiveData<BreakTime?>()

    val breaks = database.getAllBreaks()

    val startButtonVisible = Transformations.map(latestBreak) {
        null == it
    }
    val stopButtonVisible = Transformations.map(latestBreak) {
        null != it
    }

    val clearButtonVisible = Transformations.map(breaks) {
        it?.isNotEmpty()

    }

    //Snackbar
    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    fun onClear() {
        uiScope.launch {
            // Clear the database table.
            clear()

            // And clear tonight since it's no longer in the database
            latestBreak.value = null
        }

        // Show a snackbar message, because it's friendly.
        _showSnackbarEvent.value = true
    }

// time
companion object {
    // These represent different important times
    // This is when the game is over
    // This is the number of milliseconds in a second
    const val ONE_SECOND = 1000L
    // This is the total time of the game
    const val COUNTDOWN_TIME = 86400000L

    const val  FIRST_WARNING_TIME = 1800L
    const val  SECOND_WARNING_TIME = 3600L
    const val  FINAL_WARNING_TIME =  5400L


}
    private val timer: CountDownTimer

private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)

    }



    // navigation livedata




    // initsilize database
    private fun initializeLatestBreak() {
        uiScope.launch {
            latestBreak.value = getLatestFromDatabase()

        }
    }
    init {

        initializeLatestBreak()
        timer = object : CountDownTimer(COUNTDOWN_TIME , ONE_SECOND){
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (COUNTDOWN_TIME - millisUntilFinished)/ ONE_SECOND
                if ((COUNTDOWN_TIME - millisUntilFinished) / ONE_SECOND == FIRST_WARNING_TIME ){
                    _buzzEvent.value = BuzzType.FIRST

                }
                if ((COUNTDOWN_TIME - millisUntilFinished) / ONE_SECOND == SECOND_WARNING_TIME){
                    _buzzEvent.value = BuzzType.SECOND
                }
                if ((COUNTDOWN_TIME - millisUntilFinished) / ONE_SECOND == FINAL_WARNING_TIME){
                    _buzzEvent.value = BuzzType.FINAL
                }

            }

        }

    }
    private val _navigateToBreakDetail = MutableLiveData<Long>()
    val navigateToBreakDetail
        get() = _navigateToBreakDetail
    fun onBreakDetailClicked(id: Long) {
        _navigateToBreakDetail.value = id
    }
    private val _navigateToFocusQuality = MutableLiveData<BreakTime>()
    val navigateToFocusQuality: LiveData<BreakTime>
        get() = _navigateToFocusQuality
    fun doneNavigating() {
        _navigateToFocusQuality.value = null
    }
    fun onBreakDetailNavigated(){
        _navigateToBreakDetail.value = null
    }
    //


    // button synlighed
    fun onStartTracking() {
         uiScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            var newBreak = BreakTime()
            newBreak = setData(newBreak)


            insert(newBreak)

            latestBreak.value = getLatestFromDatabase()

            timer.start()


        }
    }


    fun onStopTracking() {
        uiScope.launch {
            // In Kotlin, the return@label syntax is used for specifying which function among
            // several nested ones this statement returns from.
            // In this case, we are specifying to return from launch(),
            // not the lambda.
            val previousBreakTime = latestBreak.value ?: return@launch

            // Update the night in the database to add the end time.
            previousBreakTime.timeEnd = System.currentTimeMillis()

            update(previousBreakTime)


            _navigateToFocusQuality.value = previousBreakTime
            timer.cancel()

            // Set state to navigate to the SleepQualityFragment.
        }
    }



    // set data
    private fun setData(b:BreakTime): BreakTime {
        val light = IntArray(100){
            it + 1
        }
        val noise = IntArray(100){
            it + 1
        }
        val sleepHour = IntArray(12){
            it + 1
        }
        val temperature = IntArray(41){
          -11 +  it + 1
        }


        b.lightLevel = light.random()
        b.noiseLevel = noise.random()
        b.sleepHours = sleepHour.random()
        b.temperatureLevel = temperature.random()

        return b
    }

    //db calls
    private suspend fun getLatestFromDatabase(): BreakTime? {
        return withContext(Dispatchers.IO) {
            var breakTime = database.getLatestBreak()
            if (breakTime?.timeEnd != breakTime?.timeStart) {
                breakTime = null
            }
            breakTime
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun update(breakTime: BreakTime) {
        withContext(Dispatchers.IO) {
            database.update(breakTime)
        }
    }

    private suspend fun insert(breakTime: BreakTime) {
        withContext(Dispatchers.IO) {
            database.insert(breakTime)
        }
    }

}