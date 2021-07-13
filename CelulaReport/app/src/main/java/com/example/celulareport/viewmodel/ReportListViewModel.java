package com.example.celulareport.viewmodel;

import android.app.Application;

import com.example.celulareport.db.ReportsRepository;
import com.example.celulareport.db.model.ReportCard;
import com.example.celulareport.db.model.ReportEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ReportListViewModel extends AndroidViewModel {
    private final ReportsRepository mRepository;

    public ReportListViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = ReportsRepository.getInstance(application);
    }

    public LiveData<List<ReportCard>> getReportsByMonth(String month){
        return mRepository.getReportsByMonth(month);
    }

    public void Insert(ReportEntity reportEntity){
        mRepository.Insert(reportEntity);
    }

    public void deleteById(long id){
        mRepository.deleteById(id);
    }
}
