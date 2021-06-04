package com.example.celulareport.viewmodel

import com.example.celulareport.db.repositories.ReportsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celulareport.db.model.ReportEntity
import kotlinx.coroutines.launch

class ReportDetailsViewModel private constructor(private val repository: ReportsRepository) : ViewModel() {

    fun getReportById(id: Long) = repository.getById(id)

    fun updateReport(report: ReportEntity) = viewModelScope.launch { repository.update(report) }

    fun deleteReport(report: ReportEntity) = viewModelScope.launch { repository.delete(report) }

    companion object{
        @Volatile private var instance: ReportDetailsViewModel? = null

        fun getInstance(repository: ReportsRepository){
            instance?: synchronized(this){
                instance?:ReportDetailsViewModel(repository).also { instance = it }
            }
        }
    }
}