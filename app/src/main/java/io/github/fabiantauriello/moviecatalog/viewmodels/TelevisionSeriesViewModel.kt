package io.github.fabiantauriello.moviecatalog.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * viewmodel associated with tv fragment
 */
class TelevisionSeriesViewModel : ViewModel() {

    private val repository = Repository()

    private val _tvLatest = repository.tvLatest
    val tvLatest: LiveData<MediaItem>
        get() = _tvLatest

    private val _tvAiringToday = repository.tvAiringToday
    val tvAiringToday: LiveData<List<MediaItem>>
        get() = _tvAiringToday

    private val _tvOnTheAir = repository.tvOnTheAir
    val tvOnTheAir: LiveData<List<MediaItem>>
        get() = _tvOnTheAir

    private val _tvPopular = repository.tvPopular
    val tvPopular: LiveData<List<MediaItem>>
        get() = _tvPopular

    private val _tvTopRated = repository.tvTopRated
    val tvTopRated: LiveData<List<MediaItem>>
        get() = _tvTopRated

    init {
        viewModelScope.launch {
            repository.fetchTvData()
        }
    }


}