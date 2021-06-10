package com.example.celulareport.db;

import android.content.Context;

import com.example.celulareport.util.ApplicationExecutors;
import com.example.celulareport.db.model.ReportEntity;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ReportEntity.class}, version = 1, exportSchema = false)
public abstract class ReportsRoomDatabase extends RoomDatabase {

    @VisibleForTesting
    public static final String DATABASE_NAME = "db-reports";

    //Threads used
    public static ApplicationExecutors mExecutors;

    public abstract ReportDAO reportDAO();

    private static volatile ReportsRoomDatabase sInstance;

    //Verify if database was created
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static ReportsRoomDatabase getInstance(final Context context, final ApplicationExecutors executors ){
        if(sInstance == null){
            synchronized (ReportsRoomDatabase.class){
                if(sInstance == null){
                    sInstance = buildDatabase(context, executors);
                    sInstance.updateDatabaseCreated(context);
                }
            }
        }

        mExecutors = executors;
        return sInstance;
    }

    /**
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static ReportsRoomDatabase buildDatabase(final Context context,
                                              final ApplicationExecutors executors){

        RoomDatabase.Callback mCallback = new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                executors.diskIO().execute(() -> {
                    // Add a delay to simulate a long-running operation
                    addDelay();
                    // Generate the data for pre-population
                    ReportsRoomDatabase database =  ReportsRoomDatabase.getInstance(context, executors);
                    // notify that the database was created and it's ready to be used
                    database.setDatabaseCreated();

                });

            }
        };

        return Room.databaseBuilder(context, ReportsRoomDatabase.class, DATABASE_NAME)
                .addCallback(mCallback).build();
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }
}
