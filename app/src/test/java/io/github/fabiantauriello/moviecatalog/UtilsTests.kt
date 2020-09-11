package io.github.fabiantauriello.moviecatalog

import io.github.fabiantauriello.moviecatalog.data.database.MediaItemEntity
import io.github.fabiantauriello.moviecatalog.data.database.WatchlistEntity
import io.github.fabiantauriello.moviecatalog.domain.MediaDetails
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.util.Utils.asEntityModels
import io.github.fabiantauriello.moviecatalog.util.Utils.asWatchlistEntity
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class UtilsTests {

    private lateinit var tvItems: List<MediaItem>
    private lateinit var movieItems: List<MediaItem>
    private lateinit var mediaDetails: MediaDetails

    @Before
    fun setUp() {
        tvItems = listOf(
            MediaItem(
                1,
                "Big Bang Theory",
                "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
                false
            ),
            MediaItem(
                2,
                "Seinfeld",
                "/kqjL17yufvq9OVLyx49ftyrssak.jpg",
                false
            )
        )
        movieItems = listOf(
            MediaItem(
                1,
                "Godfather 2",
                "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
                true
            ),
            MediaItem(
                2,
                "Godzilla",
                "/kqjL17yufvq9OVLyx49ftyrssak.jpg",
                true
            )
        )
        mediaDetails = MediaDetails(5, "Inception", "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg", "This movie is really good.")

    }

    @Test
    fun testMediaItemsToEntityModelConversion() {
        val convertedTvItems = tvItems.asEntityModels(false)
        val convertedMovieItems = movieItems.asEntityModels(true)

        assertThat(convertedTvItems[0], instanceOf(MediaItemEntity::class.java))
        assertThat(convertedMovieItems[0], instanceOf(MediaItemEntity::class.java))
    }

    @Test
    fun testMediaDetailsToWatchlistEntity() {
        val convertedMedia = mediaDetails.asWatchlistEntity(true)

        assertThat(convertedMedia, instanceOf(WatchlistEntity::class.java))
    }

}