package io.github.fabiantauriello.moviecatalog.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_item")
data class MediaItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mediaId: Int?,
    val title: String?,
    val posterPath: String?,
    val isMovie: Boolean
)

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey
    val mediaId: Int?,
    val title: String?,
    val posterPath: String?,
    val overview: String?,
    val isMovie: Boolean
)