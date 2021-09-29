package com.trigonated.gamecollection.model.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.trigonated.gamecollection.model.database.objects.DbGameCollectionStatus
import com.trigonated.gamecollection.model.database.objects.DbGameCollectionStatusWithPlatform
import kotlinx.coroutines.flow.Flow

@Dao
interface GameCollectionStatusDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(item: DbGameCollectionStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<DbGameCollectionStatus>)

    @Delete
    suspend fun delete(item: DbGameCollectionStatus)

    @Transaction
    @Query("SELECT * FROM game_collection_status WHERE gameId = :gameId")
    fun getGameCollectionStatuses(gameId: String): Flow<List<DbGameCollectionStatusWithPlatform>>
}