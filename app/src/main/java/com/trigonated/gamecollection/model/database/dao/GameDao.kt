package com.trigonated.gamecollection.model.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.trigonated.gamecollection.model.database.objects.DbGame
import com.trigonated.gamecollection.model.database.objects.DbGameWithPlatforms
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(game: DbGame)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<DbGame>)

    @Delete
    suspend fun delete(game: DbGame)

    @Query("SELECT * FROM games WHERE id = :gameId")
    fun get(gameId: String): Flow<DbGameWithPlatforms?>

    @Transaction
    @Query("SELECT * FROM games WHERE id IN (SELECT DISTINCT(gameId) FROM game_collection_status WHERE wishlisted = 1)")
    fun getWishlistedGames(): Flow<List<DbGameWithPlatforms>>

    @Transaction
    @Query("SELECT * FROM games WHERE id IN (SELECT DISTINCT(gameId) FROM game_collection_status WHERE owned = 1)")
    fun getOwnedGames(): Flow<List<DbGameWithPlatforms>>
}