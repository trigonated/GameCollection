package com.trigonated.gamecollection.model.database.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trigonated.gamecollection.model.objects.Platform

@Entity(tableName = "platforms")
data class DbPlatform(
    @PrimaryKey val id: String,
    val name: String
) {
    constructor(platform: Platform) : this(
        id = platform.id,
        name = platform.name
    )
}