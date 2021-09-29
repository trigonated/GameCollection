package com.trigonated.gamecollection.model

import com.trigonated.gamecollection.api.rawg.RawgService
import com.trigonated.gamecollection.api.rawg.objects.RawgGame
import com.trigonated.gamecollection.api.rawg.objects.RawgPaginatedResponse
import com.trigonated.gamecollection.api.rawg.objects.RawgPlatform
import com.trigonated.gamecollection.misc.Result
import com.trigonated.gamecollection.model.database.*
import com.trigonated.gamecollection.model.database.dao.GameCollectionStatusDao
import com.trigonated.gamecollection.model.database.dao.GameDao
import com.trigonated.gamecollection.model.database.dao.GamePlatformDao
import com.trigonated.gamecollection.model.database.dao.PlatformDao
import com.trigonated.gamecollection.model.database.objects.DbGame
import com.trigonated.gamecollection.model.database.objects.DbGameCollectionStatus
import com.trigonated.gamecollection.model.database.objects.DbGamePlatform
import com.trigonated.gamecollection.model.database.objects.DbPlatform
import com.trigonated.gamecollection.model.objects.Game
import com.trigonated.gamecollection.model.objects.GameCollectionStatus
import com.trigonated.gamecollection.model.objects.Platform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The application's data repository.
 */
@Singleton
class DataRepository @Inject constructor(
    private val gameDao: GameDao,
    private val platformDao: PlatformDao,
    private val gamePlatformDao: GamePlatformDao,
    private val gameCollectionStatusDao: GameCollectionStatusDao,
    private val rawgService: RawgService
) {
    /** Whether some required initial data has been loaded or not. */
    private var isInitialDataLoaded: Boolean = false

    /** The date of the last update.
     *
     * This is used to know whether the lists of games have been changed and their UI must be refreshed.
     */
    var lastUpdateDate: Long = System.currentTimeMillis()
        private set

    /**
     * Load some required initial data. Call this to guarantee that some ref data (e.g. the list of
     * platforms) has been loaded.
     */
    private suspend fun loadInitialDataIfRequired() {
        if (!isInitialDataLoaded) {
            fetchPlatforms().singleOrNull()
            isInitialDataLoaded = true
        }
    }

    /**
     * Call this to notify that data has been updated.
     *
     * @see lastUpdateDate
     */
    private fun notifyDataUpdated() {
        lastUpdateDate = System.currentTimeMillis()
    }

    /**
     * Fetches the list of platforms.
     */
    fun fetchPlatforms(): Flow<Result<List<Platform>>> {
        return flow<Result<List<Platform>>> {
            // Load data from db
            val dbResult: List<Platform>? =
                platformDao.getAll().firstOrNull()?.map { Platform(it) }
            if ((dbResult != null) && (dbResult.isNotEmpty())) {
                // Success
                emit(Result.success(dbResult))
                return@flow
            }
            // No data on the db. Load data from the API
            emit(Result.loading())
            val apiResponse: Response<RawgPaginatedResponse<RawgPlatform>> =
                rawgService.getPlatforms(page = 1, pageSize = 100)
            if ((apiResponse.isSuccessful) && (apiResponse.body() != null)) {
                // Save data to db
                val platforms: List<Platform> = apiResponse.body()!!.results.map { Platform(it) }
                platformDao.insertAll(platforms.map { DbPlatform(it) })
                // Success
                emit(Result.success(platforms))
            } else {
                // Failure
                emit(Result.error(message = apiResponse.message(), error = null))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Search for games based on a [query].
     * @param query The search query.
     */
    fun searchForGames(query: String): Flow<Result<List<Game>>> {
        return flow<Result<List<Game>>> {
            emit(Result.loading())

            loadInitialDataIfRequired()

            // Return nothing when the query is blank, avoiding calling the API.
            if (query.isBlank()) {
                emit(Result.success(listOf()))
                return@flow
            }
            // Call the API
            val response: Response<RawgPaginatedResponse<RawgGame>> =
                rawgService.getGames(page = 1, pageSize = 20, search = query)
            if (response.isSuccessful) {
                // Success
                emit(Result.success(response.body()?.results?.map { Game(it) }))
            } else {
                // Failure
                emit(Result.error(message = response.message(), error = null))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Fetch a specific game, based on it's id.
     * @param gameId The game's id.
     */
    fun fetchGame(gameId: String): Flow<Result<Game>> {
        return flow<Result<Game>> {
            emit(Result.loading())

            loadInitialDataIfRequired()

            // Load from db
            val dbResult: Game? =
                gameDao.get(gameId = gameId).map { if (it != null) Game(it) else null }
                    .firstOrNull()
            if (dbResult != null) {
                // Success
                emit(Result.success(dbResult))
                return@flow
            }
            // No data on the db. Load data from the API
            val apiResponse: Response<RawgGame> = rawgService.getGame(id = gameId)
            if ((apiResponse.isSuccessful) && (apiResponse.body() != null)) {
                // Success
                val game = Game(apiResponse.body()!!)
                emit(Result.success(game))
                // Save data to db
                gameDao.insert(DbGame(game))
                gamePlatformDao.insertAll(game.platforms.map {
                    DbGamePlatform(
                        gameId = game.id,
                        it.id
                    )
                })
            } else {
                // Failure
                emit(Result.error(message = apiResponse.message(), error = null))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Fetch the list games on the wishlist.
     */
    fun fetchWishlistedGames(): Flow<Result<List<Game>>> {
        return flow<Result<List<Game>>> {
            emit(Result.loading())

            loadInitialDataIfRequired()

            // Load from db
            val dbResult: List<Game>? = gameDao.getWishlistedGames().firstOrNull()?.map { Game(it) }

            if (dbResult != null) {
                // Success
                emit(Result.success(dbResult))
                return@flow
            } else {
                // Failure
                emit(Result.error(message = null, error = null))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Fetch the list of owned games.
     */
    fun fetchOwnedGames(): Flow<Result<List<Game>>> {
        return flow<Result<List<Game>>> {
            emit(Result.loading())

            loadInitialDataIfRequired()

            // Load from db
            val dbResult: List<Game>? = gameDao.getOwnedGames().firstOrNull()?.map { Game(it) }

            if (dbResult != null) {
                // Success
                emit(Result.success(dbResult))
                return@flow
            } else {
                // Failure
                emit(Result.error(message = null, error = null))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Fetch the list of the "collection status" of a game.
     * @param gameId The id of the game.
     */
    fun fetchGameCollectionStatus(gameId: String): Flow<Result<List<GameCollectionStatus>>> {
        return flow {
            emit(Result.loading())

            loadInitialDataIfRequired()

            // Load the game from the db
            val fetchGameResult: Result<Game>? =
                fetchGame(gameId).firstOrNull { it.status == Result.Status.SUCCESS }
            if (fetchGameResult?.data == null) {
                // Failed to load the game
                emit(
                    Result.error(
                        message = fetchGameResult?.message ?: "",
                        error = fetchGameResult?.error
                    )
                )
                return@flow
            }
            val game: Game = fetchGameResult.data
            // Load the statuses from the db
            val dbStatuses: List<GameCollectionStatus> =
                gameCollectionStatusDao.getGameCollectionStatuses(gameId = gameId).first()
                    .map { GameCollectionStatus(game, it) }
            // Create the final list of statuses, creating the ones that don't exist
            // (game versions that were never wishlisted/owned do not exist in the db)
            val statuses: List<GameCollectionStatus> = game.platforms.map { p ->
                dbStatuses.find { it.platform.id == p.id } ?: GameCollectionStatus(game, p)
            }
            // Success
            emit(Result.success(statuses))
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Update a collection status of a game version.
     *
     * Note: Usually a game is not wishlisted and owned at the same time, but the repository and db
     * support it.
     * @param game The game.
     * @param platform The game version platform.
     * @param wishlisted Whether the game is wishlisted or not.
     * @param owned Whether the game is owned or not.
     */
    suspend fun setGameCollectionStatus(
        game: Game,
        platform: Platform,
        wishlisted: Boolean,
        owned: Boolean
    ) {
        gameCollectionStatusDao.insert(
            DbGameCollectionStatus(
                gameId = game.id,
                platformId = platform.id,
                wishlisted = wishlisted,
                owned = owned
            )
        )
        notifyDataUpdated()
    }
}