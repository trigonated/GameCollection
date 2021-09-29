package com.trigonated.gamecollection.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
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
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataRepository: DataRepository
) : DataViewModel<List<Game>>(dataRepository) {
    /** Navigation parameter - (Optional) The initial query. */
    private val initialQuery: String = savedStateHandle["query"] ?: ""

    /** The current query. */
    var query: String = initialQuery

    /** The list of search results. */
    val items: LiveData<List<Game>> = data.mapNotNull { it.data }.asLiveData()

    override fun initialData(): Result<List<Game>> = Result.success(null)

    override fun fetchData(refreshing: Boolean): Flow<Result<List<Game>>> =
        dataRepository.searchForGames(query = query)

    override fun hasData(data: List<Game>?): Boolean = ((data == null) || (data.isNotEmpty()))

    /** Executes a search based on the [query]. */
    fun search() = loadData()
}