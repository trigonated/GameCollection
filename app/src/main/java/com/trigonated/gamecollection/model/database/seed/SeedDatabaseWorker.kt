package com.trigonated.gamecollection.model.database.seed

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.trigonated.gamecollection.model.database.AppDatabase
import com.trigonated.gamecollection.model.database.seed.SeedDatabaseWorker.Companion.createWorkerParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "SeedDatabaseWorker"

/**
 * [CoroutineWorker] that seeds the database with some data loaded from a seed json file.
 *
 * Use [createWorkerParams] to create the input data for this worker.
 */
class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        /** WorkerParameters keys for the filename of the seed file. */
        const val KEY_FILENAME = "DATA_FILENAME"

        /**
         * Create input data for this worker.
         * @param filename Filename of the seed file.
         */
        fun createWorkerParams(filename: String): Data = workDataOf(
            KEY_FILENAME to filename
        )
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Load the input data
            val filename = inputData.getString(KEY_FILENAME)

            if (filename != null) {
                // Load the seed file and seed the database
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        // Load the seed data from the seed json.
                        val databaseSeedType = object : TypeToken<DatabaseSeed>() {}.type
                        val seedData: DatabaseSeed = Gson().fromJson(jsonReader, databaseSeedType)
                        // Insert the data in the database
                        val db: AppDatabase = AppDatabase.getInstance(applicationContext)
                        db.gameDao().insertAll(seedData.games)
                        db.platformDao().insertAll(seedData.platforms)
                        db.gamePlatformDao().insertAll(seedData.gamePlatforms)
                        db.gameCollectionStatusDao().insertAll(seedData.gameCollectionStatuses)

                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }
}