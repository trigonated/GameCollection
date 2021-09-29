package com.trigonated.gamecollection.model.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.trigonated.gamecollection.model.database.objects.DbGamePlatform
import kotlinx.coroutines.flow.Flow

@Dao
interface GamePlatformDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(game: DbGamePlatform)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gamePlatforms: List<DbGamePlatform>)

    @Delete
    suspend fun delete(game: DbGamePlatform)

    @Transaction
    @Query("DELETE FROM game_platforms WHERE gameId = :gameId")
    suspend fun deleteGamePlatformsOfGame(gameId: String)

    @Transaction
    @Query("SELECT * FROM game_platforms WHERE gameId = :gameId")
    fun getGamePlatforms(gameId: String): Flow<List<DbGamePlatform>>
}