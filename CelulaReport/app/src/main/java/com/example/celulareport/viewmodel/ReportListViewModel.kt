package com.example.celulareport.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celulareport.db.model.ReportCard
import com.example.celulareport.db.model.ReportEntity
import com.example.celulareport.db.repositories.ReportsRepository
import kotlinx.coroutines.launch

class ReportListViewModel private constructor(private val repository: ReportsRepository) : ViewModel() {

    fun getReportsByMonth(month: String) = repository.getByMonth(month)

    fun insert(report: ReportEntity) {
        viewModelScope.launch {
            repository.insert(report)
        }
    }

    fun deleteReports(vararg report: ReportEntity) {
        viewModelScope.launch {
            repository.deleteAll(*report)
        }
    }


    companion object{

        @Volatile
        private var instace: ReportListViewModel? = null

        fun getInstance(repository: ReportsRepository):ReportListViewModel{
            return instace?: synchronized(this){
                instace?: ReportListViewModel(repository).also { instace = it }
            }
        }
    }
}