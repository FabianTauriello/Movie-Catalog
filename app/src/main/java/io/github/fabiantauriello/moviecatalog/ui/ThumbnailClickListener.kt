package io.github.fabiantauriello.moviecatalog.ui

import io.github.fabiantauriello.moviecatalog.domain.MediaItem

/**
 * click listener interface for the user interaction with media item thumbnails
 */
interface ThumbnailClickListener {
    fun onThumbnailClick(mediaItem: MediaItem)
}