package com.trigonated.gamecollection.model.database.objects

import androidx.room.Embedded
import androidx.room.Relation

data class DbGameCollectionStatusWithPlatform(
    @Embedded
    val gameCollectionStatus: DbGameCollectionStatus,

    @Relation(
        parentColumn = "platformId",
        entityColumn = "id"
    )
    val platform: DbPlatform
)