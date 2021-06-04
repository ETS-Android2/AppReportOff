package com.example.celulareport.db.dao

import androidx.lifecycle.LiveData
import com.example.celulareport.db.model.ReportEntity

/**
 * Base interface for Dao to be implemented for all Dao
**/
interface BaseDao<T> {

    fun getById(id: Long): LiveData<T?>

    suspend fun insertAll(vararg entity: T)

    suspend fun insert(entity: T)

    suspend fun update(entity: T)

    suspend fun deleteAll(vararg entity: T)

    suspend fun delete(entity: T)
}