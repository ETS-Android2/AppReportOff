package com.example.celulareport.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.celulareport.db.dao.ReportDAO
import com.example.celulareport.db.model.ReportEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Date

@Database(entities = [ReportEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reportDAO(): ReportDAO

    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

    /**
     * Check whether the database already exists and expose it via [.getDatabaseCreated]
     */
    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

    val databaseCreated: LiveData<Boolean>
        get() = mIsDatabaseCreated

    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }

    companion object {

        @VisibleForTesting
        val DATABASE_NAME = "db-reports"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope) : AppDatabase{
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context, scope).also { instance = it }
            }
        }

        /**
         * creates a new instance of the database.
         * The SQLite database is only created when it's accessed for the first time.
         */
        private fun buildDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addCallback(object : Callback() {

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                             scope.launch {
                                 prepopulate()
                             }
                        }
                    }).build()
        }

        private suspend fun prepopulate(){

            val PREPOPULATE_DATA:Array<ReportEntity> = arrayOf(
                ReportEntity(
                    nomeLider = "Foo Serra",
                    nomeColider = "Bar Fernandes",
                    nomeAnfitriao = "Foo Serra",
                    dataReuniao = Date.valueOf("2020-01-21"),
                    nomeCelula = "Geração Bar",
                    numMembros = 4,
                    numVisitantes = 3,
                    oferta = 13.50F,
                    comentarios = "Lucas 12:4"),

                ReportEntity(
                    nomeLider = "Baz Serra",
                    nomeColider = "Bar Rodigues",
                    nomeAnfitriao = "Foo Serra",
                    dataReuniao = Date.valueOf("2021-01-03"),
                    nomeCelula = "Geração Baz",
                    numMembros = 3,
                    numVisitantes = 5,
                    oferta = 15.50F,
                    comentarios = "João 3:4"),

                ReportEntity(
                    nomeLider = "Foz Serra",
                    nomeColider = "Baz Rodigues",
                    nomeAnfitriao = "Foz Serra",
                    dataReuniao = Date.valueOf("2020-01-28"),
                    nomeCelula = "Geração Faz",
                    numMembros = 5,
                    numVisitantes = 6,
                    oferta = 23.50F,
                    comentarios = "Marcos 3:4")
            )

            instance!!.reportDAO().insertAll(*PREPOPULATE_DATA)
        }
    }
}