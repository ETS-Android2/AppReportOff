package com.example.celulareport.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.celulareport.db.model.ReportCard
import com.example.celulareport.db.model.ReportEntity

@Dao
interface ReportDao:BaseDao<ReportEntity> {

    //data_reuniao default shape 'YY-MM-DD'
    @Query("SELECT id, celula, lider, data FROM report_table WHERE strftime('%m', data) = :month")
    fun getReportsByMonth(month: String): LiveData<List<ReportCard?>>

    @Query("SELECT id, celula, lider, data FROM report_table")
    fun getReportsCard():LiveData<List<ReportCard?>>

    //Selected a Report requested
    @Query("SELECT * FROM report_table WHERE id = :id")
    override fun getById(id: Long): LiveData<ReportEntity?>

    //Insert report added
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertAll(vararg entity: ReportEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: ReportEntity)

    @Update
    override suspend fun update(entity: ReportEntity)

    @Delete
    override suspend fun deleteAll(vararg entity:ReportEntity)

    @Delete
    override suspend fun delete(entity:ReportEntity)
}