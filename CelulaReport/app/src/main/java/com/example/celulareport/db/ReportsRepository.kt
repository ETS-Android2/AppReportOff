package com.example.celulareport.db


import androidx.lifecycle.LiveData
import com.example.celulareport.db.model.ReportCard
import com.example.celulareport.db.model.ReportEntity

class ReportsRepository private constructor(val reportDAO: ReportDAO) {

    /**
     * Get the list of reports by month from the database.
     */
    fun getReportsByMonth(month: String?) = reportDAO.getReportsByMonth(month)

    fun ReportSelected(id: Long) = reportDAO.selectedReport(id)

    suspend fun Insert(reportEntity: ReportEntity) = reportDAO.Insert(reportEntity)

    fun deleteById(id: Long) = reportDAO.deleteById(id)

    companion object {
        //singleton instance
        @Volatile private var instance: ReportsRepository? = null

        fun getInstance(reportDAO: ReportDAO) =
            instance?: synchronized(this){
                instance?: ReportsRepository(reportDAO).also { instance = it } }
    }
}