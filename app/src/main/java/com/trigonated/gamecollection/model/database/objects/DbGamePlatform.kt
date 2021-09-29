package com.trigonated.gamecollection.model.database.objects

import androidx.room.Entity

@Entity(
    tableName = "game_platforms",
    primaryKeys = ["gameId", "platformId"],
)
data class DbGamePlatform(
    val gameId: String,
    val platformId: String
)