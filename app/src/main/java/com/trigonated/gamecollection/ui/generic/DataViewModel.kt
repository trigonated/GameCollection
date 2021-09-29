package com.trigonated.gamecollection.ui.generic

import androidx.lifecycle.*
import com.trigonated.gamecollection.misc.Result
import com.trigonated.gamecollection.model.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Generic base ViewModel with a convenient setup for a ViewModel that loads some data.
 * @param T The type of data.
 */
abstract class DataViewModel<T>(
    val dataRepository: DataRepository
) : ViewModel() {
    /** The data. */
    @Suppress("LeakingThis")
    protected val data: MutableStateFlow<Result<T>> = MutableStateFlow(initialData())

    /** The data's loading status. */
    val loadingStatus: LiveData<Result.Status> = data.map { it.status }.asLiveData()

    /** Whether the data has been loaded successfully. */
    val isLoaded: LiveData<Boolean> = loadingStatus.map { it == Result.Status.SUCCESS }

    /** Whether the data is loading. */
    val isLoading: LiveData<Boolean> = loadingStatus.map { it == Result.Status.LOADING }

    /** Whether an error occurred while loading the data. */
    val isError: LiveData<Boolean> = loadingStatus.map { it == Result.Status.ERROR }

    /**
     * Whether the data is not "empty" (useful for when [data] is a list).
     * Use this to show a different "no items" UI.
     */
    val hasData: LiveData<Boolean> = data.map { hasData(it.data) }.asLiveData()

    /** The last repository data update this ViewModel knows about. */
    private var lastDataUpdateDate: Long = 0

    /**
     * Whether [lastDataUpdateDate] is the same as the repository's.
     *
     * Useful for refreshing the data when the repository changes.
     */
    protected val isDataUpToDate: Boolean
        get() = (lastDataUpdateDate == dataRepository.lastUpdateDate)

    /**
     * Load the data.
     *
     * Note: This needs to be called manually, typically on init { }.
     * @param refreshing Whether this is a refresh or an "initial loading".
     */
    protected fun loadData(refreshing: Boolean = false) {
        lastDataUpdateDate = dataRepository.lastUpdateDate
        // Fetch the data and push it to [data]
        viewModelScope.launch {
            fetchData(refreshing).collect {
                data.value = it
            }
        }
    }

    /**
     * Refresh the data.
     *
     * Note: This is protected on purpose, to avoid exposing a refresh function on ViewModels that
     * can't refresh.
     */
    protected fun refreshData() = loadData(refreshing = true)

    /**
     * Override to change the initial state of [data].
     *
     * It's a loading state by default.
     */
    protected open fun initialData(): Result<T> = Result.loading()

    /**
     * Fetch the data. This is called by [loadData] and the result is set to [data] automatically.
     * @param refreshing Whether this is a refresh or an "initial loading".
     */
    protected abstract fun fetchData(refreshing: Boolean = false): Flow<Result<T>>

    /**
     * Whether the data is not "empty".
     * @param data The data loaded from [fetchData].
     */
    protected abstract fun hasData(data: T?): Boolean
}