package com.example.celulareport.viewmodel;

import android.app.Application;

import com.example.celulareport.db.ReportsRepository;
import com.example.celulareport.db.model.ReportEntity;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ReportsViewModel extends AndroidViewModel {
    private static final String QUERY_KEY = "QUERY";
    private long mReportID;
    private final ReportsRepository mRepository;

    public ReportsViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = ReportsRepository.getInstance(application);
    }

    public void update(ReportEntity reportEntity){
        this.mRepository.update(reportEntity);
    }

    public LiveData<ReportEntity> ReportSelected(final long mReportID){
        return mRepository.ReportSelected(mReportID);
    }

    public void deleteById(long id){
        mRepository.deleteById(id);
    }
}
