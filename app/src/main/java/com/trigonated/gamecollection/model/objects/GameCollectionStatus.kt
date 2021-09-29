package com.trigonated.gamecollection.model.objects

import com.trigonated.gamecollection.model.database.objects.DbGameCollectionStatus
import com.trigonated.gamecollection.model.database.objects.DbGameCollectionStatusWithPlatform

/**
 * Stores the "collection status" (owned/wishlisted) of a game in a specific platform.
 *
 * @See com.trigonated.gamecollection.model.DataRepository.setGameCollectionStatus
 */
data class GameCollectionStatus constructor(
    /** The game. */
    val game: Game,
    /** The specific platform version this status relates to. */
    val platform: Platform,
    /** Whether this game is owned. */
    val owned: Boolean,
    /** Whether this game is on the wishlist. */
    val wishlisted: Boolean
) {
    constructor(game: Game, status: DbGameCollectionStatus) : this(
        game = game,
        platform = game.platforms.find { it.id == status.platformId }!!,
        owned = status.owned,
        wishlisted = status.wishlisted
    )

    constructor(game: Game, status: DbGameCollectionStatusWithPlatform) : this(
        game = game,
        status = status.gameCollectionStatus
    )

    constructor(game: Game, platform: Platform) : this(
        game = game,
        platform = platform,
        owned = false,
        wishlisted = false
    )
}