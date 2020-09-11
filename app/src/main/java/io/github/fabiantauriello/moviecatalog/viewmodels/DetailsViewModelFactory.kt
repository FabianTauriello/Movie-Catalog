package io.github.fabiantauriello.moviecatalog.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * factory class for details view model
 */
class DetailsViewModelFactory(val mediaId: Int?, val isMovie: Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(mediaId, isMovie) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}