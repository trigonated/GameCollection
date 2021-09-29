package com.trigonated.gamecollection.model.objects

import com.trigonated.gamecollection.api.rawg.objects.RawgGamePlatform
import com.trigonated.gamecollection.api.rawg.objects.RawgPlatform
import com.trigonated.gamecollection.model.database.objects.DbPlatform

/**
 * Represents a game platform (pc, console, etc).
 */
class Platform private constructor(
    /** An unique id for the platform. This is formatted like a "slug". */
    val id: String,
    /** The name of the platform. */
    val name: String
) {
    constructor(platform: DbPlatform) : this(
        id = platform.id,
        name = platform.name
    )

    constructor(platform: RawgPlatform) : this(
        id = platform.slug,
        name = platform.name
    )

    constructor(gamePlatform: RawgGamePlatform) : this(
        id = gamePlatform.platform.slug,
        name = gamePlatform.platform.name
    )
}