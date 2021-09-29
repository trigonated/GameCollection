package com.trigonated.gamecollection.ui.gamedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.trigonated.gamecollection.misc.Result
import com.trigonated.gamecollection.misc.SingleLiveEvent
import com.trigonated.gamecollection.model.DataRepository
import com.trigonated.gamecollection.model.objects.Game
import com.trigonated.gamecollection.ui.generic.DataViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataRepository: DataRepository
) : DataViewModel<Game>(dataRepository) {
    /** Navigation parameter - The id of the game. */
    val gameId: String = savedStateHandle["gameId"]!!

    /** The game. */
    val game: LiveData<Game> = data.mapNotNull { it.data }.asLiveData()

    /** Fired when the viewmodel wants to navigate to the game collection status. */
    val onNavigateToGameCollectionStatusRequested = SingleLiveEvent<Unit>()

    /** Fired when the viewmodel wants to share an url. */
    val onShareUrlRequested = SingleLiveEvent<ShareUrlInfo>()

    /** Fired when the viewmodel wants to open an url in the browser. */
    val onOpenUrlRequested = SingleLiveEvent<String>()

    init {
        loadData()
    }

    override fun fetchData(refreshing: Boolean): Flow<Result<Game>> =
        dataRepository.fetchGame(gameId)

    override fun hasData(data: Game?): Boolean = (data != null)

    /** Navigate to the game collection status. */
    fun showGameCollectionStatus() = onNavigateToGameCollectionStatusRequested.call()

    /** Share the details of the game. */
    fun shareGame() {
        val game: Game = this.game.value ?: return
        onShareUrlRequested.call(
            ShareUrlInfo(
                title = game.name,
                url = game.url ?: return
            )
        )
    }

    /** Open the game's page in the browser. */
    fun openGameInBrowser() {
        val game: Game = this.game.value ?: return
        onOpenUrlRequested.call(
            game.url ?: return
        )
    }

    /** Holds data related to [onShareUrlRequested]. */
    inner class ShareUrlInfo(
        /** The title shown to the user. */
        val title: String,
        /** The url of the content being shared. */
        val url: String
    )
}