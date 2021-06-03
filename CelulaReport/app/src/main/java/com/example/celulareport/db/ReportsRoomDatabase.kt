package com.example.celulareport.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.celulareport.db.model.ReportEntity
import com.example.celulareport.util.ApplicationExecutors

@Database(entities = [ReportEntity::class], version = 1, exportSchema = false)
abstract class ReportsRoomDatabase : RoomDatabase() {
    abstract fun reportDAO(): ReportDAO?

    //Verify if database was created
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

        //Threads used
        var mExecutors: ApplicationExecutors? = null

        @Volatile
        private var sInstance: ReportsRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context, executors: ApplicationExecutors): ReportsRoomDatabase? {
            if (sInstance == null) {
                synchronized(ReportsRoomDatabase::class.java) {
                    if (sInstance == null) {
                        sInstance = buildDatabase(context, executors)
                        sInstance!!.updateDatabaseCreated(context)
                    }
                }
            }
            mExecutors = executors
            return sInstance
        }

        /**
         * creates a new instance of the database.
         * The SQLite database is only created when it's accessed for the first time.
         */
        private fun buildDatabase(
            context: Context,
            executors: ApplicationExecutors
        ): ReportsRoomDatabase {
            val mCallback: Callback = object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    executors.diskIO().execute {

                        // Add a delay to simulate a long-running operation
                        addDelay()
                        // Generate the data for pre-population
                        val database = getInstance(context, executors)
                        // notify that the database was created and it's ready to be used
                        database!!.setDatabaseCreated()
                    }
                }
            }

            return Room.databaseBuilder(context, ReportsRoomDatabase::class.java, DATABASE_NAME)
                .addCallback(mCallback).build()
        }

        private fun addDelay() {
            try {
                Thread.sleep(4000)
            } catch (ignored: InterruptedException) {
            }
        }
    }
}