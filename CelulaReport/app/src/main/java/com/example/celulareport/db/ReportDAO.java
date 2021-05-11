package com.example.celulareport.db;

import com.example.celulareport.db.model.ReportCard;
import com.example.celulareport.db.model.ReportEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ReportDAO {

    //data_reuniao default shape 'YY-MM-DD'
    @Query("SELECT id, celula, lider, data_reuniao FROM report_table WHERE strftime('%m', data_reuniao) = :month")
    LiveData<List<ReportCard>> getReportsByMonth(String month);

    @Query("SELECT id, celula, lider, data_reuniao FROM report_table")
    LiveData<List<ReportCard>> getReportsByMonth();

    //Selected a Report requested
    @Query("SELECT * FROM report_table WHERE id = :id")
    LiveData<ReportEntity> selectedReport(long id);

    //Insert report added
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void Insert(ReportEntity reportEntity);

    @Query("DELETE FROM report_table")
    void deleteAll();

    @Query("DELETE FROM report_table WHERE id = :id")
    void deleteById(long id);

}
