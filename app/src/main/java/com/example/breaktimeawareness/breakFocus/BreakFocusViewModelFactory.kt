package com.example.breaktimeawareness.breakFocus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breaktimeawareness.database.BreakTimeDatabaseDao

class BreakFocusViewModelFactory(
    private val breakTimeKey: Long,
    private val dataSource: BreakTimeDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BreakFocusViewModel::class.java)) {
            return BreakFocusViewModel(breakTimeKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}