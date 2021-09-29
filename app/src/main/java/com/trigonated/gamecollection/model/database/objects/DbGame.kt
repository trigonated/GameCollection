package com.trigonated.gamecollection.model.database.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trigonated.gamecollection.model.objects.Game

@Entity(tableName = "games")
data class DbGame(
    @PrimaryKey val id: String,
    val url: String?,
    val name: String,
    val imageUrl: String = "",
    val description: String?,
    val publisher: String?
) {
    constructor(game: Game) : this(
        id = game.id,
        url = game.url,
        name = game.name,
        imageUrl = game.imageUrl,
        description = game.description,
        publisher = game.publisher
    )
}