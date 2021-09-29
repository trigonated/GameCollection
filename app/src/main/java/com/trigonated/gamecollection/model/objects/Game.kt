package com.trigonated.gamecollection.model.objects

import com.trigonated.gamecollection.api.rawg.objects.RawgGame
import com.trigonated.gamecollection.model.database.objects.DbGameWithPlatforms

/**
 * Represents a game.
 */
data class Game constructor(
    /** An unique id for the game. This is formatted like a "slug". */
    val id: String,
    /** An url used for showing game info in a browser. */
    val url: String?,
    /** The name of the game. */
    val name: String,
    /** The url of the game's image. Leave empty for games with no image. */
    val imageUrl: String = "",
    /** A description of the game. */
    val description: String?,
    /** The name of the game's publisher. */
    val publisher: String?,
    /** The list of platforms this game supports. */
    val platforms: List<Platform>
) {
    constructor(game: DbGameWithPlatforms) : this(
        id = game.game.id,
        url = game.game.url,
        name = game.game.name,
        imageUrl = game.game.imageUrl,
        description = game.game.description,
        publisher = game.game.publisher,
        platforms = game.platforms.map { Platform(it) }
    )

    constructor(game: RawgGame) : this(
        id = game.slug,
        url = game.url,
        name = game.name,
        imageUrl = game.backgroundImage ?: "",
        description = game.description,
        publisher = null,
        platforms = game.platforms?.map { Platform(it) } ?: listOf()
    )
}