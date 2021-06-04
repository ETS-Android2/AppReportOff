package com.example.celulareport.db.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.sql.Date
import java.util.*

//BOJO to list of reports contents that from related month
data class ReportCard(

    val id: Long,

    @ColumnInfo(name = "celula")
    var nomeCelula: String,

    @ColumnInfo(name = "lider")
    var nomeLider: String,

    @ColumnInfo(name = "data")
    private var dataReuniao: Date

) {
    override fun toString(): String {
        return dataReuniao.toString()
    }
}