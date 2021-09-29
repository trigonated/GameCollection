package com.trigonated.gamecollection.api.rawg

import com.trigonated.gamecollection.BuildConfig
import com.trigonated.gamecollection.api.rawg.objects.RawgGame
import com.trigonated.gamecollection.api.rawg.objects.RawgPaginatedResponse
import com.trigonated.gamecollection.api.rawg.objects.RawgPlatform
import com.trigonated.gamecollection.misc.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * A Retrofit service interface for the RAWG API.
 *
 * [Official doc](https://api.rawg.io/docs)
 */
interface RawgService {
    /**
     * [Official doc](https://api.rawg.io/docs/#operation/platforms_list)
     */
    @GET("platforms")
    suspend fun getPlatforms(
        @Query("key") key: String = BuildConfig.RAWG_API_KEY,
//        @Query("ordering") ordering: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<RawgPaginatedResponse<RawgPlatform>>

    /**
     * [Official doc](https://api.rawg.io/docs/#operation/games_list)
     */
    @GET("games")
    suspend fun getGames(
        @Query("key") key: String = BuildConfig.RAWG_API_KEY,
//        @Query("ordering") ordering: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("search") search: String
    ): Response<RawgPaginatedResponse<RawgGame>>

    /**
     * [Official doc](https://api.rawg.io/docs/#operation/games_read)
     */
    @GET("games/{id}")
    suspend fun getGame(
        @Path("id") id: String,
        @Query("key") key: String = BuildConfig.RAWG_API_KEY
    ): Response<RawgGame>

    companion object {
        private const val BASE_URL = Constants.RAWG_API_BASE_URL

        fun create(): RawgService {
            // Setup the OkHttp client
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            // Create the service
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RawgService::class.java)
        }
    }
}