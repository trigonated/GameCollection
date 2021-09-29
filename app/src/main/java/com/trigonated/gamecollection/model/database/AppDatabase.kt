package com.trigonated.gamecollection.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.trigonated.gamecollection.BuildConfig
import com.trigonated.gamecollection.misc.Constants
import com.trigonated.gamecollection.model.database.dao.GameCollectionStatusDao
import com.trigonated.gamecollection.model.database.dao.GameDao
import com.trigonated.gamecollection.model.database.dao.GamePlatformDao
import com.trigonated.gamecollection.model.database.dao.PlatformDao
import com.trigonated.gamecollection.model.database.objects.DbGame
import com.trigonated.gamecollection.model.database.objects.DbGameCollectionStatus
import com.trigonated.gamecollection.model.database.objects.DbGamePlatform
import com.trigonated.gamecollection.model.database.objects.DbPlatform
import com.trigonated.gamecollection.model.database.seed.SeedDatabaseWorker

/**
 * The Room database for this app.
 */
@Database(
    entities = [DbGame::class, DbPlatform::class, DbGamePlatform::class, DbGameCollectionStatus::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun platformDao(): PlatformDao
    abstract fun gamePlatformDao(): GamePlatformDao
    abstract fun gameCollectionStatusDao(): GameCollectionStatusDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Returns an instance of this database, building it if required.
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        /**
         * Builds the database object.
         */
        private fun buildDatabase(context: Context): AppDatabase {
            val db = Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        /**
                         * Called when the database is being created anew.
                         */
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // Seed the database with some initial data using a worker.
                            if (BuildConfig.SEED_DATABASE) {
                                val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                                    .setInputData(SeedDatabaseWorker.createWorkerParams(Constants.DATABASE_SEED))
                                    .build()
                                WorkManager.getInstance(context).enqueue(request)
                            }
                        }
                    }
                )
                .build()
            // Do a select to force the database to be created here
            db.query("select 1", null)
            return db
        }
    }
}