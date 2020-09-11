package io.github.fabiantauriello.moviecatalog.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.repository.Repository
import kotlinx.coroutines.launch

/**
 * viewmodel associated with movies fragment
 */
class MoviesViewModel : ViewModel() {

    private val repository = Repository()

    private val _movieLatest = repository.moviesLatest
    val movieLatest: LiveData<MediaItem>
        get() = _movieLatest

    private val _moviesNowPlaying = repository.moviesNowPlaying
    val moviesNowPlaying: LiveData<List<MediaItem>>
        get() = _moviesNowPlaying

    private val _moviesPopular = repository.moviesPopular
    val moviesPopular: LiveData<List<MediaItem>>
        get() = _moviesPopular

    private val _moviesTopRated = repository.moviesTopRated
    val moviesTopRated: LiveData<List<MediaItem>>
        get() = _moviesTopRated

    private val _moviesUpcoming = repository.moviesUpcoming
    val moviesUpcoming: LiveData<List<MediaItem>>
        get() = _moviesUpcoming

    init {
        // run api call with viewmodelscope to avoid leaks
        viewModelScope.launch {
            repository.fetchMovieData()
        }
    }


}