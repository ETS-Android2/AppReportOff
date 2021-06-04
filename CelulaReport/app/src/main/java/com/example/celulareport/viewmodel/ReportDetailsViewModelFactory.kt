package com.example.celulareport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.celulareport.db.repositories.ReportsRepository
import java.lang.IllegalArgumentException

class ReportDetailsViewModelFactory(private val repository: ReportsRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ReportDetailsViewModel::class.java)){
                return ReportDetailsViewModel.getInstance(repository) as T
            }
        throw IllegalArgumentException("Unknown ${ReportDetailsViewModel::class.java} class")
    }
}