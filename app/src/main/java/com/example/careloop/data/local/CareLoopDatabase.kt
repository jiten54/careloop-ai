package com.example.careloop.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.careloop.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Patient::class,
        CarePlan::class,
        CareTask::class,
        MedicalDocument::class,
        MedicationReminder::class,
        OperationalMetric::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CareLoopDatabase : RoomDatabase() {
    abstract fun careLoopDao(): CareLoopDao

    companion object {
        @Volatile
        private var INSTANCE: CareLoopDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CareLoopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CareLoopDatabase::class.java,
                    "careloop_database"
                )
                .addCallback(CareLoopDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class CareLoopDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.careLoopDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: CareLoopDao) {
            dao.insertPatients(SampleData.samplePatients)
            dao.insertCarePlans(SampleData.sampleCarePlans)
            dao.insertCareTasks(SampleData.sampleCareTasks)
            dao.insertDocuments(SampleData.sampleDocuments)
            dao.insertReminders(SampleData.sampleReminders)
            dao.insertOperationalMetrics(SampleData.sampleMetrics)
        }
    }
}
