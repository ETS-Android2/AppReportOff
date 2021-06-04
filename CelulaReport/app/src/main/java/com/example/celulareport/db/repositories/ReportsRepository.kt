package com.example.celulareport.db.repositories


import androidx.lifecycle.LiveData
import com.example.celulareport.db.dao.ReportDao
import com.example.celulareport.db.model.ReportEntity

class ReportsRepository private constructor(private val reportDao: ReportDao): BaseRepository<ReportEntity> {
    /**
     * Get the list of reports by month from the database.
     */
    fun getByMonth(month: String) = reportDao.getReportsByMonth(month)

    override fun getById(id: Long): LiveData<ReportEntity?> {
        return reportDao.getById(id)
    }

    override suspend fun update(entity: ReportEntity) {
        reportDao.update(entity)
    }

    override suspend fun insertAll(vararg entities: ReportEntity) {
        reportDao.insertAll(*entities)
    }

    override suspend fun insert(entity: ReportEntity) {
        reportDao.insert(entity)
    }

    override suspend fun deleteAll(vararg entities: ReportEntity) {
        reportDao.deleteAll(*entities)
    }

    override suspend fun delete(entity: ReportEntity) {
        reportDao.delete(entity)
    }

    companion object {
        //singleton instance
        @Volatile private var instance: ReportsRepository? = null

        fun getInstance(reportDAO: ReportDao): ReportsRepository{
            return instance ?: synchronized(this){
                instance ?: ReportsRepository(reportDAO).also { instance = it }
            }
        }
    }
}