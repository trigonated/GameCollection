package com.trigonated.gamecollection.api.rawg.objects

import com.google.gson.annotations.SerializedName
import com.trigonated.gamecollection.misc.Constants

/**
 * Represents a game object in the RAWG API.
 *
 * [Official doc](https://api.rawg.io/docs/#operation/games_read)
 */
data class RawgGame(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("slug") val slug: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("description") val description: String?,
    @field:SerializedName("released") val released: String,
    @field:SerializedName("tba") val tba: Boolean,
    @field:SerializedName("background_image") val backgroundImage: String?,
    @field:SerializedName("rating") val rating: Double,
    @field:SerializedName("rating_top") val ratingTop: Int,
    @field:SerializedName("reviews_text_count") val reviewsTextCount: String,
    @field:SerializedName("added") val added: Int,
    @field:SerializedName("metacritic") val metacritic: Int,
    @field:SerializedName("playtime") val playtime: Int,
    @field:SerializedName("suggestions_count") val suggestionsCount: Int,
    @field:SerializedName("updated") val updated: String,
    @field:SerializedName(
        value = "platforms",
        alternate = ["parent_platforms"]
    ) val platforms: List<RawgGamePlatform>?
) {
    val url: String
        get() = Constants.RAWG_GAME_BASE_URL + slug
}

/**
 * A platform of a game object in the RAWG API.
 *
 * [Official doc](https://api.rawg.io/docs/#operation/games_read)
 */
data class RawgGamePlatform(
    @field:SerializedName("platform") val platform: RawgPlatform,
    @field:SerializedName("released_at") val releasedAt: String
)