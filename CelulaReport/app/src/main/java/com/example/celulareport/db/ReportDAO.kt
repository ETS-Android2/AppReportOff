package com.example.celulareport.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.celulareport.db.model.ReportCard
import com.example.celulareport.db.model.ReportEntity

@Dao
interface ReportDAO {

    //data_reuniao default shape 'YY-MM-DD'
    @Query("SELECT id, celula, lider, data FROM report_table WHERE strftime('%m', data) = :month")
    fun getReportsByMonth(month: String?): LiveData<List<ReportCard?>?>?

    @get:Query("SELECT id, celula, lider, data FROM report_table")
    val reportsByMonth: LiveData<List<ReportCard?>?>?

    //Selected a Report requested
    @Query("SELECT * FROM report_table WHERE id = :id")
    fun selectedReport(id: Long): LiveData<ReportEntity?>?

    //Insert report added
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun Insert(reportEntity: ReportEntity?)

    @Query("DELETE FROM report_table WHERE id = :id")
    fun deleteById(id: Long)
}