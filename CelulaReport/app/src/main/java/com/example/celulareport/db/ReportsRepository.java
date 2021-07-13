package com.example.celulareport.db;

import android.app.Application;


import com.example.celulareport.db.model.ReportEntity;
import com.example.celulareport.db.model.ReportCard;
import com.example.celulareport.util.ApplicationExecutors;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ReportsRepository {

    //singleton instance
    private static ReportsRepository sInstance;

    private final ReportsRoomDatabase mReportDatabase;

    private final ApplicationExecutors mExecutors;

    public ReportsRepository(final Application application){
        mExecutors = new ApplicationExecutors();
        mReportDatabase = ReportsRoomDatabase.getInstance(application, mExecutors);
    }

    public static ReportsRepository getInstance(final Application application){
        if (sInstance == null){
            synchronized (ReportsRepository.class){
                if(sInstance == null){
                    sInstance = new ReportsRepository(application);
                }
            }
        }

        return sInstance;
    }


    /**
     * Get the list of reports by month from the database.
     */
    public LiveData<List<ReportCard>> getReportsByMonth(String month){
        return mReportDatabase.reportDAO().getReportsByMonth(month);
    }

    public LiveData<ReportEntity> reportSelected(long id){
        return mReportDatabase.reportDAO().selectedReport(id);
    }

    public void Insert(ReportEntity reportEntity){
        mExecutors.diskIO().execute(() -> mReportDatabase.reportDAO().Insert(reportEntity));
    }

    public void update(ReportEntity reportEntity){
        mExecutors.diskIO().execute(() -> mReportDatabase.reportDAO().update(reportEntity));
    }

    public void deleteAll(){
        mExecutors.diskIO().execute(() -> mReportDatabase.reportDAO().deleteAll());
    }

    public void deleteById(long id){
        mExecutors.diskIO().execute(()-> mReportDatabase.reportDAO().deleteById(id));
    }

}
