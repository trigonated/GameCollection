package com.trigonated.gamecollection.model.database.objects

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DbGameWithPlatforms(
    @Embedded
    val game: DbGame,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = DbGamePlatform::class,
            parentColumn = "gameId",
            entityColumn = "platformId"
        )
    )
    val platforms: List<DbPlatform>
)