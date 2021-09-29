package com.trigonated.gamecollection.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.trigonated.gamecollection.model.database.objects.DbPlatform
import kotlinx.coroutines.flow.Flow

@Dao
interface PlatformDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(platform: DbPlatform)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(platforms: List<DbPlatform>)

    @Delete
    suspend fun delete(platform: DbPlatform)

    @Query("SELECT * FROM platforms")
    fun getAll(): Flow<List<DbPlatform>>

    @Query("SELECT * FROM platforms WHERE id = :platformId")
    fun get(platformId: String): Flow<DbPlatform>
}