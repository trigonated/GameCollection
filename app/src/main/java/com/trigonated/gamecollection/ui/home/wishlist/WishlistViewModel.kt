package com.trigonated.gamecollection.ui.home.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.trigonated.gamecollection.misc.Result
import com.trigonated.gamecollection.model.DataRepository
import com.trigonated.gamecollection.model.objects.Game
import com.trigonated.gamecollection.ui.generic.DataViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    dataRepository: DataRepository
) : DataViewModel<List<Game>>(dataRepository) {
    /** The list of owned games. */
    val items: LiveData<List<Game>> = data.mapNotNull { it.data }.asLiveData()

    init {
        loadData()
    }

    override fun fetchData(refreshing: Boolean): Flow<Result<List<Game>>> {
        return dataRepository.fetchWishlistedGames()
    }

    override fun hasData(data: List<Game>?): Boolean = !data.isNullOrEmpty()

    fun onResume() {
        // Check if the data changed and refresh if it did.
        if (!isDataUpToDate) refreshData()
    }
}