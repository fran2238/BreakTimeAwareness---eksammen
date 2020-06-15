package com.example.breaktimeawareness.breakDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breaktimeawareness.database.BreakTimeDatabaseDao

class BreakDetailViewModelFactory(
    private val breakTimeKey: Long,
    private val dataSource: BreakTimeDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BreakDetailViewModel::class.java)) {
            return BreakDetailViewModel(breakTimeKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}