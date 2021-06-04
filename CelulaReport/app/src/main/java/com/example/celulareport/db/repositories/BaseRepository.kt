package com.example.celulareport.db.repositories

import androidx.lifecycle.LiveData
import com.example.celulareport.db.dao.BaseDao
import com.example.celulareport.db.model.ReportEntity

/**
 * Abstractions of repository .
 **/

interface BaseRepository<T>{

    fun getById(id: Long):LiveData<T?>

    suspend fun update(entity: T)

    suspend fun insertAll(vararg entities: T)

    suspend fun insert(entity: T)

    suspend fun deleteAll(vararg entities: T)

    suspend fun delete(entity: T)
}