package com.example.celulareport.util

import android.content.Context
import com.example.celulareport.db.AppDatabase
import com.example.celulareport.db.repositories.ReportsRepository
import com.example.celulareport.viewmodel.ReportDetailsViewModelFactory
import com.example.celulareport.viewmodel.ReportListViewModelFactory
import kotlinx.coroutines.CoroutineScope

object InjectUtil {

    private fun getRepository(context: Context, scope: CoroutineScope):ReportsRepository{
        return ReportsRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext, scope).reportDAO()
        )
    }

    fun createListViewModelFactory(context: Context, scope: CoroutineScope): ReportListViewModelFactory{
        return ReportListViewModelFactory(getRepository(context, scope))
    }

    fun createDetailsViewModelFactory(context: Context, scope: CoroutineScope): ReportDetailsViewModelFactory{
        return ReportDetailsViewModelFactory(getRepository(context, scope))
    }
}