package com.trigonated.gamecollection.model.database.seed

import com.trigonated.gamecollection.model.database.objects.DbGame
import com.trigonated.gamecollection.model.database.objects.DbGameCollectionStatus
import com.trigonated.gamecollection.model.database.objects.DbGamePlatform
import com.trigonated.gamecollection.model.database.objects.DbPlatform

/**
 * Object that represents the deserialized data loaded from the seed file.
 */
data class DatabaseSeed(
    val games: List<DbGame>,
    val platforms: List<DbPlatform>,
    val gamePlatforms: List<DbGamePlatform>,
    val gameCollectionStatuses: List<DbGameCollectionStatus>,
)