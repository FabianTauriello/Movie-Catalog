package io.github.fabiantauriello.moviecatalog.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.repository.Repository

/**
 * viewmodel associated with watch list fragment
 */
class WatchlistViewModel : ViewModel() {

    private val repository = Repository()

    private val _watchlist = repository.watchList
    val watchlist: LiveData<List<MediaItem>>
        get() = _watchlist


}