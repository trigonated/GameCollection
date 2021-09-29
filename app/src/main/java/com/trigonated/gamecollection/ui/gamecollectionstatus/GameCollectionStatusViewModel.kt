package com.trigonated.gamecollection.ui.gamecollectionstatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.trigonated.gamecollection.misc.Result
import com.trigonated.gamecollection.model.DataRepository
import com.trigonated.gamecollection.model.objects.GameCollectionStatus
import com.trigonated.gamecollection.ui.generic.DataViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameCollectionStatusViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataRepository: DataRepository
) : DataViewModel<List<GameCollectionStatus>>(dataRepository) {
    /** Navigation parameter - The id of the game. */
    private val gameId: String = savedStateHandle["gameId"]!!

    /** The list of collection statuses. */
    val items: LiveData<List<GameCollectionStatus>> = data.mapNotNull { it.data }.asLiveData()

    init {
        loadData()
    }

    override fun fetchData(refreshing: Boolean): Flow<Result<List<GameCollectionStatus>>> {
        return dataRepository.fetchGameCollectionStatus(gameId = gameId)
    }

    override fun hasData(data: List<GameCollectionStatus>?): Boolean = !data.isNullOrEmpty()

    /**
     * Toggle the "wishlisted" status of an item.
     * @param item The item to change.
     */
    fun toggleItemWishlistedStatus(item: GameCollectionStatus) {
        setItemStatus(item, wishlisted = !item.wishlisted, owned = false)
    }

    /**
     * Toggle the "owned" status of an item.
     * @param item The item to change.
     */
    fun toggleItemOwnedStatus(item: GameCollectionStatus) {
        setItemStatus(item, wishlisted = false, owned = !item.owned)
    }

    /**
     * Changes the collection status of an item.
     *
     * Note: An item can only be wishlisted or owned, not both at the same time.
     * @param item The item to change.
     * @param wishlisted Whether it's wishlisted or not.
     * @param owned Whether it's owned or not.
     * @throws IllegalArgumentException If [wishlisted] and [owned] are both true.
     */
    private fun setItemStatus(
        item: GameCollectionStatus,
        wishlisted: Boolean,
        owned: Boolean
    ) {
        // Check for invalid arguments
        if ((wishlisted) && (owned)) throw IllegalArgumentException("wishlisted and owned cannot be both true")
        // Change the collection status and refresh
        viewModelScope.launch {
            dataRepository.setGameCollectionStatus(
                game = item.game,
                platform = item.platform,
                wishlisted = wishlisted,
                owned = owned
            )
            refreshData()
        }
    }
}