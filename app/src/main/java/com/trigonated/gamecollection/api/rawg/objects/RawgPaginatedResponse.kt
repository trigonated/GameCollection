package com.trigonated.gamecollection.api.rawg.objects

import com.google.gson.annotations.SerializedName

/**
 * Generic class that represents "paginated" responses from the RAWG API.
 */
data class RawgPaginatedResponse<T>(
    @field:SerializedName("count") val count: Int,
    @field:SerializedName("next") val next: String?,
    @field:SerializedName("previous") val previous: String?,
    @field:SerializedName("results") val results: List<T>
)