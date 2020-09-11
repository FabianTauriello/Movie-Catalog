package io.github.fabiantauriello.moviecatalog.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.fabiantauriello.moviecatalog.domain.MediaDetails
import io.github.fabiantauriello.moviecatalog.repository.Repository
import kotlinx.coroutines.launch

/**
 * view model associated with details fragment
 */
class DetailsViewModel(val mediaId: Int?, val isMovie: Boolean) : ViewModel() {

    private val repository = Repository()

    private val _mediaDetails = repository.mediaDetails
    val mediaDetails: LiveData<MediaDetails>
        get() = _mediaDetails

    // this property is bound to the toggle button in the details fragment layout
    private val _isInWatchlist = repository.isInWatchlist
    val isInWatchlist: LiveData<Boolean>
        get() = _isInWatchlist

    init {
        repository.checkIfMediaIsInWatchlist(mediaId, isMovie)

        // run api call with viewmodelscope to avoid leaks
        viewModelScope.launch {
            // set default state for toggle button
            repository.fetchMediaDetails(mediaId, isMovie)
        }

    }

}