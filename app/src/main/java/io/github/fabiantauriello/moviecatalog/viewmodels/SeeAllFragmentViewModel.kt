package io.github.fabiantauriello.moviecatalog.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.fabiantauriello.moviecatalog.data.RemoteMediator
import io.github.fabiantauriello.moviecatalog.data.database.MediaItemEntity
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * viewmodel associated with see all fragment
 */
class SeeAllFragmentViewModel : ViewModel() {

    private val repository = Repository()

    private var currentResult: Flow<PagingData<MediaItemEntity>>? = null

    private val _searchResults = repository.searchResults
    val searchResults: LiveData<List<MediaItem>>
        get() = _searchResults

    fun retrieveMediaItemsAsFlow(endpoint: String): Flow<PagingData<MediaItemEntity>> {

        val lastResult = currentResult

        if (lastResult != null) {
            return lastResult
        }

        val newResult = repository.createPagerFlow(endpoint, RemoteMediator(endpoint)).cachedIn(viewModelScope)
        currentResult = newResult

        return newResult
    }

    fun searchApi(endpoint: String, searchQuery: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.searchApi(endpoint, searchQuery)
        }
    }


}