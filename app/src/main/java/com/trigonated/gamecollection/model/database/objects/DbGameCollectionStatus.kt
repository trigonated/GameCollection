package com.trigonated.gamecollection.model.database.objects

import androidx.room.Entity

@Entity(
    tableName = "game_collection_status",
    primaryKeys = ["gameId", "platformId"],
)
data class DbGameCollectionStatus(
    val gameId: String,
    val platformId: String,
    val owned: Boolean = false,
    val wishlisted: Boolean = false
)