package io.github.fabiantauriello.moviecatalog.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.data.database.MediaItemEntity
import io.github.fabiantauriello.moviecatalog.data.database.WatchlistEntity
import io.github.fabiantauriello.moviecatalog.domain.MediaDetails
import io.github.fabiantauriello.moviecatalog.domain.MediaItem

private const val POSTER_IMAGE_BASE_URL: String = "https://image.tmdb.org/t/p/"
private const val POSTER_IMAGE_SIZE: String = "w342"

/**
 * several extension functions to make converting between domain/model objects and
 * setting images easier.
 */
object Utils {

    fun setThumbnailImage(posterPath: String?, imageView: ImageView) {
        // create options
        val options: RequestOptions = RequestOptions()
            .override(450, 600)
            .error(R.drawable.ic_broken_image)

        // set image
        Glide.with(imageView.context)
            .load(POSTER_IMAGE_BASE_URL + POSTER_IMAGE_SIZE + posterPath)
            .apply(options)
            .into(imageView)
    }

    fun setDetailsBannerImage(posterPath: String?, imageView: ImageView) {
        val options = RequestOptions()
            .error(R.drawable.ic_broken_image)

        Glide.with(imageView.context)
            .load(POSTER_IMAGE_BASE_URL + POSTER_IMAGE_SIZE + posterPath)
            .apply(options)
            .into(imageView)
    }

    fun List<MediaItem>.asEntityModels(isMovie: Boolean): List<MediaItemEntity> {
        return map {
            MediaItemEntity(
                mediaId = it.mediaId,
                title = it.title,
                posterPath = it.posterPath,
                isMovie = isMovie
            )
        }
    }

    fun MediaDetails?.asWatchlistEntity(isMovie: Boolean): WatchlistEntity {
        return WatchlistEntity(
            mediaId = this?.mediaId,
            title = this?.title,
            posterPath = this?.posterPath,
            overview = this?.overview,
            isMovie = isMovie
        )
    }

}