package com.trigonated.gamecollection.api.rawg.objects

import com.google.gson.annotations.SerializedName

/**
 * Represents a platform object in the RAWG API.
 *
 * [Official doc](https://api.rawg.io/docs/#operation/platforms_read)
 */
data class RawgPlatform(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("slug") val slug: String,
    @field:SerializedName("games_count") val gamesCount: Int?,
    @field:SerializedName("image_background") val imageBackground: String?,
    @field:SerializedName("image") val image: String?,
    @field:SerializedName("year_start") val yearStart: Int?,
    @field:SerializedName("year_end") val yearEnd: Int?
)